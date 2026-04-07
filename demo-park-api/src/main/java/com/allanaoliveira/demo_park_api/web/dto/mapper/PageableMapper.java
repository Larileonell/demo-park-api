package com.allanaoliveira.demo_park_api.web.dto.mapper;

import com.allanaoliveira.demo_park_api.web.dto.PageableDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class PageableMapper {
    public static PageableDto pageabletoDto(Page pageable) {
        return  new ModelMapper().map(pageable, PageableDto.class);
        //Esse método converte um objeto Page em um objeto PageableDto usando a biblioteca ModelMapper.
    }
}
