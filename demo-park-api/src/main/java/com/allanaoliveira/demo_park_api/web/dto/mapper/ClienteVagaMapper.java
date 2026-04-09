package com.allanaoliveira.demo_park_api.web.dto.mapper;

import com.allanaoliveira.demo_park_api.entity.ClienteVaga;
import com.allanaoliveira.demo_park_api.web.dto.EstaciomentoCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.EstacionamentoRespondeDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstaciomentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }
    public static EstacionamentoRespondeDto toDto (ClienteVaga clienteVaga) {
        return new ModelMapper().map(clienteVaga, EstacionamentoRespondeDto.class);
    }
}
