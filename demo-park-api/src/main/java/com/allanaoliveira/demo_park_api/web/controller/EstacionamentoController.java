package com.allanaoliveira.demo_park_api.web.controller;

import com.allanaoliveira.demo_park_api.entity.ClienteVaga;
import com.allanaoliveira.demo_park_api.service.ClienteVagaService;
import com.allanaoliveira.demo_park_api.service.EstacionamentoService;
import com.allanaoliveira.demo_park_api.web.dto.EstaciomentoCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.EstacionamentoRespondeDto;
import com.allanaoliveira.demo_park_api.web.dto.mapper.ClienteVagaMapper;
import com.allanaoliveira.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operações de registro e entrada e saída de veículos no estacionamento")
@RestController
@RequestMapping("/api/v1/estacionamento")
@RequiredArgsConstructor
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;

    @Operation(summary = "Operação de Check-in", description = "Recurso para dar entrada de um veículo no estacionamento"
            + "Requisição exige uso de um bear token. acesso restrito a role= 'ADMIN'",
            security = @SecurityRequirement(name = "Security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Check-in realizado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URI do recurso criado"),
                            content = @Content(mediaType = "application/json;charset-8",
                                    schema = @Schema(implementation = EstacionamentoRespondeDto.class))),
                    @ApiResponse(responseCode = "404", description = "Causas possíveis: <br/>" + "- CPF do cliente não Cadastrado no sistema; <br/>" +
                            "- Nenhuma vaga disponível para o tipo de veículo informado",
                            content = @Content(mediaType = "application/json;charset-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json;charset-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfuil CLIENTE",
                            content = @Content(mediaType = "application/json;charset-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<EstacionamentoRespondeDto> checkIn (@RequestBody @Valid EstaciomentoCreateDto dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoRespondeDto responseDto =  ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}").
                buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).build();

    }
    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENTE') ")
    public ResponseEntity<EstacionamentoRespondeDto> getByRecibo (@PathVariable String recibo){
        ClienteVaga clienteVaga = clienteVagaService.findByRecibo(recibo);
        EstacionamentoRespondeDto responseDto =  ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(responseDto);

    }

}
