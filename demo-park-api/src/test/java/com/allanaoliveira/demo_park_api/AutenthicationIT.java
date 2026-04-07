package com.allanaoliveira.demo_park_api;


import com.allanaoliveira.demo_park_api.jwt.JwtToken;
import com.allanaoliveira.demo_park_api.web.dto.UserLoginDto;
import com.allanaoliveira.demo_park_api.web.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AutenthicationIT {

    @Autowired
    WebTestClient webTestClient;


    @Test
    public void autenticar_usuario_com_sucesso(){

        JwtToken responseBody = webTestClient.post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)//tipo
                .bodyValue(new UserLoginDto("allana@gmail.com", "123456"))//passa um valor real
                .exchange()//o que eu espero do test
                .expectStatus().isOk()//verifica o status certo
                .expectBody(JwtToken.class)//verifica o corpo da resposta, se é do tipo JwtToken
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();


    }  @Test
    public void credenciaisInvalidas_retornarErroMessage400(){

        ErrorMessage responseBody = webTestClient.post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)//tipo
                .bodyValue(new UserLoginDto("invalid@gmail.com", "123456"))//passa um valor real
                .exchange()//o que eu espero do test
                .expectStatus().isBadRequest()//verifica o status certo
                .expectBody(ErrorMessage.class)//verifica o corpo da resposta, se é do tipo JwtToken
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
        Assertions.assertThat(responseBody.getMessage()).isEqualTo("Invalid credentials");


    }
    @Test
    public void usernameInvalido_retornarErroMessage400(){

        ErrorMessage responseBody = webTestClient.post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)//tipo
                .bodyValue(new UserLoginDto("", "123456"))//passa um valor real
                .exchange()//o que eu espero do test
                .expectStatus().isEqualTo(422)//verifica o status certo
                .expectBody(ErrorMessage.class)//verifica o corpo da resposta, se é do tipo JwtToken
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMessage()).isEqualTo("Invalid credentials");

        responseBody = webTestClient.post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)//tipo
                .bodyValue(new UserLoginDto("invalid@gmail.com", "123456"))//passa um valor real
                .exchange()//o que eu espero do test
                .expectStatus().isEqualTo(422)//verifica o status certo
                .expectBody(ErrorMessage.class)//verifica o corpo da resposta, se é do tipo JwtToken
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    } @Test
    public void passwordInvalido_retornarErroMessage400(){

        ErrorMessage responseBody = webTestClient.post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)//tipo
                .bodyValue(new UserLoginDto("allana@gmail", ""))//passa um valor real
                .exchange()//o que eu espero do test
                .expectStatus().isEqualTo(422)//verifica o status certo
                .expectBody(ErrorMessage.class)//verifica o corpo da resposta, se é do tipo JwtToken
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMessage()).isEqualTo("Invalid credentials");

        responseBody = webTestClient.post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)//tipo
                .bodyValue(new UserLoginDto("allana@gmail.com", "123"))//passa um valor real
                .exchange()//o que eu espero do test
                .expectStatus().isEqualTo(422)//verifica o status certo
                .expectBody(ErrorMessage.class)//verifica o corpo da resposta, se é do tipo JwtToken
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }
}



