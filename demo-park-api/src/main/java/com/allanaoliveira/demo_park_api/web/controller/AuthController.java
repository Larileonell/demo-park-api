package com.allanaoliveira.demo_park_api.web.controller;

import com.allanaoliveira.demo_park_api.jwt.JwtToken;
import com.allanaoliveira.demo_park_api.jwt.JwtUserDetailsService;
import com.allanaoliveira.demo_park_api.web.dto.UserLoginDto;
import com.allanaoliveira.demo_park_api.web.dto.UserResponseDto;
import com.allanaoliveira.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "AuthController", description = "Controller para autenticação de usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Autenticar na API", description = "Recurso de Autenticação na API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso e retorno de um bear token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Credenciais inválidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos de entrada invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })

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
