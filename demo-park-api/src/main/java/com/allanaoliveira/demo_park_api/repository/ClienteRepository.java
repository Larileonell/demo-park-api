package com.allanaoliveira.demo_park_api.repository;

import com.allanaoliveira.demo_park_api.entity.Cliente;
import com.allanaoliveira.demo_park_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findById(Long id);
}
