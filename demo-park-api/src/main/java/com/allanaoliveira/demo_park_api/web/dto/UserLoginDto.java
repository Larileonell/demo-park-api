package com.allanaoliveira.demo_park_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank
    @Email(regexp = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Formato do e-mail está invalido")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;
}
