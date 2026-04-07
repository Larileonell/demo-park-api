package com.allanaoliveira.demo_park_api.web.controller;

import com.allanaoliveira.demo_park_api.entity.Cliente;
import com.allanaoliveira.demo_park_api.jwt.JwtUserDetails;
import com.allanaoliveira.demo_park_api.service.ClienteService;
import com.allanaoliveira.demo_park_api.service.UserService;
import com.allanaoliveira.demo_park_api.web.dto.ClienteCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.ClienteResponseDto;
import com.allanaoliveira.demo_park_api.web.dto.UserResponseDto;
import com.allanaoliveira.demo_park_api.web.dto.mapper.ClienteMapper;
import com.allanaoliveira.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Tag(name = "Cliente", description = "Endpoints relacionados a operações de Cliente")
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    private final UserService userService;

    @Operation(summary = "Criar um novo Cliente", description = "Recurso para criar um novo cliente no sistema. " +
            "O cliente deve estar associado a um usuário existente. O recurso é acessível apenas para usuários com a função 'CLIENTE'.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado para o recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto clienteCreateDto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(clienteCreateDto);
        cliente.setUser(userService.findById(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDTO(cliente));


    }
    @Operation(summary = "Localizar um Cliente pelo id", description = "Recurso para localizar um cliente pelo id do usuário associado. "
            + "Requisição exige uso de um bear token. Acesso restrito a role='ADMIN'." ,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

            })
    @PostMapping
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> findById(@PathVariable Long id ) {
        Cliente cliente = clienteService.findByUserId(id);
        return ResponseEntity.ok(ClienteMapper.toDTO(cliente));
    }

    public ResponseEntity <List<Cliente>>findAll() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

}
