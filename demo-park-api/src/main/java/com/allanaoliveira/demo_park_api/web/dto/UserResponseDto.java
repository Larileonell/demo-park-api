package com.allanaoliveira.demo_park_api.web.dto;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Integer id;
    private String username;
    private String role;
}
