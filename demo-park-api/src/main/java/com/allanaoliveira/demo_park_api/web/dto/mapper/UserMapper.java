package com.allanaoliveira.demo_park_api.web.dto.mapper;



import com.allanaoliveira.demo_park_api.web.dto.UserCreateDto;
import com.allanaoliveira.demo_park_api.entity.User;
import com.allanaoliveira.demo_park_api.web.dto.UserResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;


public class UserMapper {

    public static User toUser(UserCreateDto createDto) {
        return new ModelMapper().map(createDto, User.class);
    }

    public static UserResponseDto toDto(User user) {


        String role = user.getRole().name()
                .replace("ROLE_", "")
                .replace("CLIENT", "CLIENTE");

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                role
        );
    }

    public static List<UserResponseDto> toListDto(List<User> usuarios) {
        return usuarios.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}

