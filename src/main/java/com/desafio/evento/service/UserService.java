package com.desafio.evento.service;

import com.desafio.evento.model.User;
import com.desafio.evento.model.request.RegisterRequest;
import com.desafio.evento.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(RegisterRequest user) {
        User newUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
