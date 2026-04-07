package com.allanaoliveira.demo_park_api.web.controller;

import com.allanaoliveira.demo_park_api.entity.Cliente;
import com.allanaoliveira.demo_park_api.jwt.JwtUserDetails;
import com.allanaoliveira.demo_park_api.repository.projection.ClienteProjection;
import com.allanaoliveira.demo_park_api.service.ClienteService;
import com.allanaoliveira.demo_park_api.service.UserService;
import com.allanaoliveira.demo_park_api.web.dto.ClienteCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.ClienteResponseDto;
import com.allanaoliveira.demo_park_api.web.dto.PageableDto;
import com.allanaoliveira.demo_park_api.web.dto.UserResponseDto;
import com.allanaoliveira.demo_park_api.web.dto.mapper.ClienteMapper;
import com.allanaoliveira.demo_park_api.web.dto.mapper.PageableMapper;
import com.allanaoliveira.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Cliente", description = "Endpoints relacionados a operações de Cliente")
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    private final UserService userService;

    @Operation(summary = "Criar um novo Cliente",
            description = "Recurso para criar um novo cliente no sistema. " +
            "O cliente deve estar associado a um usuário existente. O recurso é acessível apenas para usuários com a função 'CLIENTE'.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado para o recurso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))

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


    @Operation(summary = "Localizar um Cliente pelo id",
            description = "Recurso para localizar um cliente pelo id do usuário associado. "
            + "Requisição exige uso de um bear token. Acesso restrito a role='ADMIN'." ,
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))

            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> findById(@PathVariable Long id ) {
        Cliente cliente = clienteService.findByUserId(id);
        return ResponseEntity.ok(ClienteMapper.toDTO(cliente));
    }



    @Operation(summary = "Recuperar lista de clientes",
            description = "Requisição exige uso de um bear token. Acessp restrito a Role= 'ADMIN'.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "Representa a total de elementos por página"),
                    @Parameter(in = QUERY, name = "sort",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Representa a  de ordenação dos resultados. Aceita multiplos criterios de ordenação são suportados.")
            },
            responses = {@ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAll(@Parameter(hidden = true) @PageableDefault(size =5, sort = {"nome"}) Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.pageabletoDto(clientes));
    }



    @Operation(summary = "Recuperar dados do cliente autenticado",
            description = "Requisição exige uso de um bear token. Acessp restrito a Role= 'CLIENTE'.",
            security = @SecurityRequirement(name = "security"),
            responses = {@ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de ADMIN",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getDetalhes(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = clienteService.buscarPorUsuario(userDetails.getId());
        return ResponseEntity.ok(ClienteMapper.toDTO(cliente));


}}
