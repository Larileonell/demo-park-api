package com.allanaoliveira.demo_park_api.web.controller;

import com.allanaoliveira.demo_park_api.jwt.JwtToken;
import com.allanaoliveira.demo_park_api.jwt.JwtUserDetailsService;
import com.allanaoliveira.demo_park_api.web.dto.UserLoginDto;
import com.allanaoliveira.demo_park_api.web.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid UserLoginDto  userLoginDto, HttpServletRequest request) {
        log.info("Iniciando autenticacao");
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());
              authenticationManager.authenticate(authenticationToken);
              JwtToken jwtToken = jwtUserDetailsService.getTokenAuthenticated(userLoginDto.getUsername());
              return ResponseEntity.ok(jwtToken);
        }catch (AuthenticationException ex){

            log.error("Erro ao autenticar");
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST,"Credenciais invalidas"));

        }
    }
}
