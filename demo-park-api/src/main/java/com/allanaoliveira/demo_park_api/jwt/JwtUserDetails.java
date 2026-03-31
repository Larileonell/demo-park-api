package com.allanaoliveira.demo_park_api.jwt;

import com.allanaoliveira.demo_park_api.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class JwtUserDetails extends org.springframework.security.core.userdetails.User {

    private User user;

    public JwtUserDetails(User user) {
        super(
                user.getUsername(),                 // username
                user.getPassword(),                 // password
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()))
        );
        this.user = user;
    }

    public Long getId() {
        return this.user.getId();
    }

    public String getRole() {
        return this.user.getRole().name();
    }
}
