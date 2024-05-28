package com.desafio.evento.controller;

import com.desafio.evento.model.LoginResponse;
import com.desafio.evento.security.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        com.desafio.evento.model.Authentication authRequest = new com.desafio.evento.model.Authentication("user", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authenticationService.authenticate(authentication)).thenReturn("dummy-token");

        ResponseEntity<LoginResponse> response = authenticationController.login(authRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("dummy-token", response.getBody().token());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticationService, times(1)).authenticate(authentication);
    }

    @Test
    void testLoginFailure() {
        com.desafio.evento.model.Authentication authRequest = new com.desafio.evento.model.Authentication("user", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        Exception exception = assertThrows(AuthenticationException.class, () -> {
            authenticationController.login(authRequest);
        });

        assertEquals("Bad credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticationService, times(0)).authenticate(any(Authentication.class));
    }
}

