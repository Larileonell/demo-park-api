package com.allanaoliveira.demo_park_api;

import com.allanaoliveira.demo_park_api.web.dto.UserCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.UserResponseDto;
import com.allanaoliveira.demo_park_api.web.dto.mapper.UserPasswordDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void createUserTest() throws Exception {
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

    @Test
    public void UsernameInvalidTest() throws Exception {

        UserCreateDto dto = new UserCreateDto("allana@gmail.com", "123456");

        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("allana@gmail.com"))
                .andExpect(jsonPath("$.role").value("CLIENTE"));

    }

    @Test
    public void deveRetornarBadRequest_QuandoSenhaInvalida() throws Exception {


        UserCreateDto dto = new UserCreateDto("allana@gmail.com", "123");

        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    public void deveRejeitarSenhaVazia() throws Exception {
        UserCreateDto dto = new UserCreateDto("allana@gmail.com", "");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.password").exists());
    }


    @Test
    public void deveRejeitarSenhaNull() throws Exception {

        UserCreateDto dto = new UserCreateDto("allana@gmail.com", null);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    public void deveRetornarConflict_QuandoEmailJaExiste() throws Exception {


        UserCreateDto dto = new UserCreateDto("allana@gmail.com", "123456");

        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username allana@gmail.com already exists"));


    }

    @Test
    public void deveRetornarUsuarioQuandoIdExistir() throws Exception {

        mockMvc.perform(
                        get("/api/v1/users/{id}", 100)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.username").value("allana@gmail.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    public void deveAtualizarSenhaComSucesso() throws Exception {

        UserPasswordDto dto = new UserPasswordDto(
                "senhaAtual123",
                "novaSenha123",
                "novaSenha123"
        );

        mockMvc.perform(patch("/api/v1/users/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

}
