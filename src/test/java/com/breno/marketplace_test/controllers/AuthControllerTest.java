package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.LoginRequestDTO;
import com.breno.marketplace_test.dtos.LoginResponseDTO;
import com.breno.marketplace_test.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthService service;

    @InjectMocks
    private AuthController controller;

    @Test
    void shouldLoginSuccessfully() {

        // Arrange
        LoginRequestDTO dto =
                new LoginRequestDTO("breno@gmail.com", "123456");

        String fakeToken = "jwt-token-fake";

        when(service.login(dto)).thenReturn(fakeToken);

        // Act
        ResponseEntity<LoginResponseDTO> response =
                controller.login(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(fakeToken, response.getBody().accessToken());

        assertEquals(dto.email(), response.getBody().email());
    }
}