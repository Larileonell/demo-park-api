package com.allanaoliveira.demo_park_api.web.dto.mapper;

import com.allanaoliveira.demo_park_api.entity.Vaga;
import com.allanaoliveira.demo_park_api.web.dto.VagaCreateDto;
import com.allanaoliveira.demo_park_api.web.dto.VagaResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {
    public static Vaga toDto(VagaCreateDto dto) {

        return new ModelMapper().map(dto, Vaga.class);
    }
    public static VagaResponseDto toVaga(Vaga vaga) {

        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
