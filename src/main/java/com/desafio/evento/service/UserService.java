package com.desafio.evento.service;

import com.desafio.evento.model.User;
import com.desafio.evento.model.UserRole;
import com.desafio.evento.model.request.RegisterRequest;
import com.desafio.evento.model.response.EventResponse;
import com.desafio.evento.model.response.UserResponse;
import com.desafio.evento.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse saveUser(RegisterRequest registerRequest) {
        User user = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                UserRole.valueOf(String.valueOf(registerRequest.getRole()))
        );
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);

    }

    public Optional<UserResponse> findById(String id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserResponse convertToDTO(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
