package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.AuthRequest;
import com.pragma.usuarios.application.dto.AuthResponse;
import com.pragma.usuarios.application.mapper.UserMapper;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthHandlerTest {

    @Mock
    private IAuthServicePort authServicePort;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthHandler authHandler;

    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authRequest = new AuthRequest();
        authRequest.setEmail("test@test.com");
        authRequest.setPassword("1234");

        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("1234");
    }

    @Test
    void login_ShouldReturnAuthResponse() {
        // Arrange
        when(userMapper.toUserLogin(authRequest)).thenReturn(user);
        when(authServicePort.getToken(any(User.class))).thenReturn("fake-jwt-token");
        when(authServicePort.getRoles(any(User.class))).thenReturn(List.of("OWNER"));

        // Act
        AuthResponse response = authHandler.login(authRequest);

        // Assert
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals(List.of("OWNER"), response.getRoles());
    }
}
