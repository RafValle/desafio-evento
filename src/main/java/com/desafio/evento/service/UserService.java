package com.desafio.evento.service;

import com.desafio.evento.model.User;
import com.desafio.evento.model.UserRole;
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

    public User saveUser(RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                UserRole.valueOf(String.valueOf(request.getRole()))
        );
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
