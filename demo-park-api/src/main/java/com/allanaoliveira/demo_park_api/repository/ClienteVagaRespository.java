package com.allanaoliveira.demo_park_api.repository;

import com.allanaoliveira.demo_park_api.entity.ClienteVaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteVagaRespository extends JpaRepository<ClienteVaga, Long> {
}
