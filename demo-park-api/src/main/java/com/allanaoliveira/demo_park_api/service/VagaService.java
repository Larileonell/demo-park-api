package com.allanaoliveira.demo_park_api.service;

import com.allanaoliveira.demo_park_api.entity.Vaga;
import com.allanaoliveira.demo_park_api.repository.VagaRepository;
import com.allanaoliveira.demo_park_api.web.exception.CodigUniqueViolationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VagaService {
    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga save(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigUniqueViolationException(
                    String.format("Vaga com código já cadastrado %s já existe", vaga.getCodigo()));

        }


    }
    @Transactional
    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Vaga com código %s não encontrada", codigo)));
    }

    @Transactional
    public Vaga BuscarVagaLivre() {
        return vagaRepository.findFirstByStatus(Vaga.Statusvaga.LIVRE).orElseThrow(
                () -> new EntityNotFoundException("Nenhuma vaga livre encontrada")
        );
    }
}
