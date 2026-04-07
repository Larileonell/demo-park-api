package com.allanaoliveira.demo_park_api.service;

import com.allanaoliveira.demo_park_api.entity.Cliente;
import com.allanaoliveira.demo_park_api.exception.cpfUniqueViolationException;
import com.allanaoliveira.demo_park_api.repository.ClienteRepository;
import com.allanaoliveira.demo_park_api.web.exception.EntintyNotFoudException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new cpfUniqueViolationException(String.format("PF %s já existe.", cliente.getCpf()));
        }

    }

    @Transactional
    public Cliente findByUserId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntintyNotFoudException(String.format("Cliente com id %d não encontrado.", id)));
    }

    @Transactional
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
}
