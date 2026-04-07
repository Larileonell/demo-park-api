package com.allanaoliveira.demo_park_api.web.dto.mapper;

import com.allanaoliveira.demo_park_api.entity.Cliente;
import com.allanaoliveira.demo_park_api.web.dto.ClienteCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.ClienteResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClienteMapper {
    public static Cliente toCliente(ClienteCreateDto clienteCreateDto) {
        return new ModelMapper().map(clienteCreateDto, Cliente.class);
    }
    public static ClienteResponseDto toDTO(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }
}