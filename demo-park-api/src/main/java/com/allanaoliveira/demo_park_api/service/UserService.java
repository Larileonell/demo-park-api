package com.allanaoliveira.demo_park_api.service;

import com.allanaoliveira.demo_park_api.entity.User;
import com.allanaoliveira.demo_park_api.exception.UsernameUniqueViolationException;
import com.allanaoliveira.demo_park_api.repository.UserRepository;
import com.allanaoliveira.demo_park_api.web.exception.EntintyNotFoudException;
import com.allanaoliveira.demo_park_api.web.exception.PasswordInvalidException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save (User user) {

        try {
            return userRepository.save(user);
        }catch (DataIntegrityViolationException ex){
            throw  new UsernameUniqueViolationException(String.format("Username %s already exists", user.getUsername()));
        }
    }

    @Transactional
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntintyNotFoudException(String.format("Usuário id=%s não encontrado.", id))
        );
    }


    @Transactional
    public User editPassword(Long id, String currentPassword, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password does not match the confirmation password.");
        }

        User user = findById(id);

        if (!user.getPassword().equals(currentPassword)) {
            throw new PasswordInvalidException("Current password does not match.");
        }

        user.setPassword(newPassword);
        return user;
    }


    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
