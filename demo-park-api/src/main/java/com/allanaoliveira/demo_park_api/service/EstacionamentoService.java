package com.allanaoliveira.demo_park_api.service;

import com.allanaoliveira.demo_park_api.entity.Cliente;
import com.allanaoliveira.demo_park_api.entity.ClienteVaga;
import com.allanaoliveira.demo_park_api.entity.Vaga;
import com.allanaoliveira.demo_park_api.util.EstacionamentoUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;


    @Transactional
    public ClienteVaga checkIn (ClienteVaga clienteVaga) {

        Cliente cliente = clienteService.BuscarPorCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.BuscarVagaLivre();
        vaga.setStatusVaga(Vaga.Statusvaga.OCUPADA);
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());
        clienteVaga.setRecibo(EstacionamentoUtils.GegarRecibo());

       return  clienteVagaService.save(clienteVaga);



    }
}
