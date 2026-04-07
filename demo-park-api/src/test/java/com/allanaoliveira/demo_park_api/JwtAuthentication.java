package com.allanaoliveira.demo_park_api;

import com.allanaoliveira.demo_park_api.jwt.JwtToken;
import com.allanaoliveira.demo_park_api.web.dto.UserLoginDto;
import org.springframework.test.web.reactive.server.WebTestClient;


import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

public class JwtAuthentication {
    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
       String token= client.post()
                .uri("/auth/login")
                .bodyValue(new UserLoginDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult()
                .getResponseBody()
                .getToken();




        return headers -> headers.add(
                HttpHeaders.AUTHORIZATION,
                "Bearer " + token
        );
    }

}
