package com.allanaoliveira.demo_park_api.service;

import com.allanaoliveira.demo_park_api.entity.ClienteVaga;
import com.allanaoliveira.demo_park_api.repository.ClienteVagaRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteVagaService {


    private final ClienteVagaRespository clienteVagaRepository;

    @Transactional
    public ClienteVaga save(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }
}
