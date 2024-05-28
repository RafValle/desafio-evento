package com.desafio.evento.controller;

import com.desafio.evento.model.UserRole;
import com.desafio.evento.model.request.RegisterRequest;
import com.desafio.evento.model.response.UserResponse;
import com.desafio.evento.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserById() {
        UserResponse userResponse = UserResponse.builder().id("1").username("user").role("USER").build();

        when(userService.findById("1")).thenReturn(Optional.of(userResponse));

        ResponseEntity<UserResponse> response = userController.getEventById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponse.getId(), response.getBody().getId());

        verify(userService, times(1)).findById("1");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserByIdNotFound() {
        when(userService.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userController.getEventById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService, times(1)).findById("1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUsers() {
        UserResponse user1 = UserResponse.builder().id("1").username("user1").role("USER").build();
        UserResponse user2 = UserResponse.builder().id("2").username("user2").role("USER").build();
        List<UserResponse> userList = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(userList);

        ResponseEntity<List<UserResponse>> response = userController.getUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(userService, times(1)).findAll();
    }
}