package com.allanaoliveira.demo_park_api.service;

import com.allanaoliveira.demo_park_api.entity.User;
import com.allanaoliveira.demo_park_api.exception.UsernameUniqueViolationException;
import com.allanaoliveira.demo_park_api.repository.UserRepository;
import com.allanaoliveira.demo_park_api.web.exception.EntintyNotFoudException;
import com.allanaoliveira.demo_park_api.web.exception.PasswordInvalidException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save (User user) {

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public User editPassword(
            Long id,
            String currentPassword,
            String newPassword,
            String confirmPassword
    ) {
        if(!newPassword.equals(confirmPassword)){
            throw new PasswordInvalidException("Passwords do not match");
        }
        Optional<User> user = userRepository.findById(id);
        if(!passwordEncoder.matches(currentPassword, user.get().getPassword())){
            throw new PasswordInvalidException("Current password is incorrect");
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user.get());


    }





    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Transactional
    public User.Role findByUsernameByRole(String username) {
        return userRepository.findRoleByUsername(username);

    }

}
