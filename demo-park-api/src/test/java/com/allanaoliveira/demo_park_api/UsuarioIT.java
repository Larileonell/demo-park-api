package com.allanaoliveira.demo_park_api;

import com.allanaoliveira.demo_park_api.web.dto.UserCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.UserResponseDto;
import org.assertj.core.api.Assertions;
import  static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void deveCriarUmUsuario() throws Exception {

        String json = """
            {
                "username": "allana@gmail.com",
                "password": "123456"
            }
        """;

        MvcResult result = mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        UserResponseDto dto = new ObjectMapper().readValue(response, UserResponseDto.class);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getUsername()).isEqualTo("allana@gmail.com");
        assertThat(dto.getRole()).isEqualTo("CLIENTE");
    }
}

