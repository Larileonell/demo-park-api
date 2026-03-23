package com.allanaoliveira.demo_park_api.repository;

import com.allanaoliveira.demo_park_api.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByid(Long id);
}
