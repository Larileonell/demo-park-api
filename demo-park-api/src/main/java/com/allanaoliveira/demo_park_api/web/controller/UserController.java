package com.allanaoliveira.demo_park_api.web.controller;

import com.allanaoliveira.demo_park_api.entity.User;
import com.allanaoliveira.demo_park_api.service.UserService;
import com.allanaoliveira.demo_park_api.web.dto.UserCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.UserResponseDto;
import com.allanaoliveira.demo_park_api.web.dto.mapper.UserMapper;
import com.allanaoliveira.demo_park_api.web.dto.mapper.UserPasswordDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserResponseDto> create( @Valid @RequestBody UserCreateDto userCreate) {
        User user = userService.save(UserMapper.toUser(userCreate));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @GetMapping
    public User findById( @RequestParam  Long id)  {
       return userService.findById(id);
    }


    @PatchMapping("{id}")
    public ResponseEntity<User> updatePassword(
            @PathVariable Long id,
            @RequestBody UserPasswordDto dto
    ) {


        User updatedUser = userService.editPassword(
                id,
                dto.getCurrentPassword(),
                dto.getNewPassword(),
                dto.getConfirmPassword()
        );

        return ResponseEntity.noContent().build();
    }


    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(UserMapper.toListDto(users));
    }

}
