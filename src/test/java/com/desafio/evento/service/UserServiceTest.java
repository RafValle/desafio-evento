package com.desafio.evento.service;

import com.desafio.evento.model.User;
import com.desafio.evento.model.UserRole;
import com.desafio.evento.model.request.RegisterRequest;
import com.desafio.evento.model.response.UserResponse;
import com.desafio.evento.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user")
                .password("password")
                .role(UserRole.ADMIN)
                .build();

        User user = new User(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()), UserRole.USER);
        user.setId("1");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        UserResponse userResponse = userService.saveUser(registerRequest);

        assertNotNull(userResponse);
        assertEquals(user.getId(), userResponse.getId());
        assertEquals(user.getUsername(), userResponse.getUsername());
    }

    @Test
    void testFindById() {
        String userId = "1";
        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setRole(UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<UserResponse> userResponse = userService.findById(userId);

        assertTrue(userResponse.isPresent());
        assertEquals(userId, userResponse.get().getId());
    }

    @Test
    void testFindAll() {
        User user = new User();
        user.setId("1");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setUsername("user");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        var users = userService.findAll();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }
}