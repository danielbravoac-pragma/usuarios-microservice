package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.application.exceptions.BlockedByLoginAttemptsException;
import com.pragma.usuarios.application.exceptions.InvalidCredentialsLoginException;
import com.pragma.usuarios.domain.api.IPasswordEncoderServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.usecase.AuthUseCase;
import com.pragma.usuarios.infrastructure.output.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private IUserServicePort userServicePort;
    @Mock
    private IUserRoleServicePort userRoleServicePort;
    @Mock
    private IPasswordEncoderServicePort passwordEncoder;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User userInput;
    private User userFromDb;

    @BeforeEach
    void setUp() {
        userInput = new User();
        userInput.setEmail("test@example.com");
        userInput.setPassword("plainPassword");

        userFromDb = new User();
        userFromDb.setId(1L);
        userFromDb.setEmail("test@example.com");
        userFromDb.setPassword("hashedPassword");
    }

    @Test
    void getToken_ShouldReturnToken_WhenCredentialsValid() {
        when(userServicePort.findByEmail("test@example.com")).thenReturn(userFromDb);
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);
        when(userRoleServicePort.findByUser(userFromDb)).thenReturn(List.of("ADMIN"));
        when(jwtService.generateToken(1L, List.of("ADMIN"))).thenReturn("jwt-token");

        String token = authUseCase.getToken(userInput);

        assertEquals("jwt-token", token);
        verify(jwtService).generateToken(1L, List.of("ADMIN"));
        // Verifica que resetea intentos
        assertFalse(authUseCase.isBlocked("test@example.com"));
    }

    @Test
    void getToken_ShouldThrowInvalidCredentials_WhenPasswordIncorrect() {
        when(userServicePort.findByEmail("test@example.com")).thenReturn(userFromDb);
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsLoginException.class, () -> authUseCase.getToken(userInput));
        // Verifica que incrementa intentos
        assertFalse(authUseCase.isBlocked("test@example.com"));
    }

    @Test
    void getToken_ShouldThrowBlockedException_WhenUserIsBlocked() {
        // Forzar el estado bloqueado directamente
        authUseCase.loginFailed("test@example.com");
        authUseCase.loginFailed("test@example.com");
        authUseCase.loginFailed("test@example.com");
        authUseCase.loginFailed("test@example.com");
        authUseCase.loginFailed("test@example.com");

        assertTrue(authUseCase.isBlocked("test@example.com"));
        assertThrows(BlockedByLoginAttemptsException.class, () -> authUseCase.getToken(userInput));
    }

    @Test
    void loginSucceeded_ShouldResetAttempts() {
        authUseCase.loginFailed("test@example.com");
        authUseCase.loginSucceeded("test@example.com");

        assertFalse(authUseCase.isBlocked("test@example.com"));
    }

    @Test
    void loginFailed_ShouldBlockAfterMaxAttempts() {
        for (int i = 0; i < 5; i++) {
            authUseCase.loginFailed("test@example.com");
        }

        assertTrue(authUseCase.isBlocked("test@example.com"));
    }

    @Test
    void isBlocked_ShouldReturnFalse_WhenNoAttempts() {
        assertFalse(authUseCase.isBlocked("unknown@example.com"));
    }

    @Test
    void isBlocked_ShouldReturnFalse_WhenNoLockUntil() {
        authUseCase.loginFailed("test@example.com");
        // Primer intento no tiene lockUntil
        assertFalse(authUseCase.isBlocked("test@example.com"));
    }

    @Test
    void isBlocked_ShouldReturnTrue_WhenLockUntilFuture() {
        AuthUseCase.LoginAttempt attempt = new AuthUseCase.LoginAttempt(3, LocalDateTime.now().plusMinutes(5));
        ConcurrentHashMap<String, AuthUseCase.LoginAttempt> map = new ConcurrentHashMap<>();
        map.put("test@example.com", attempt);

        ReflectionTestUtils.setField(authUseCase, "attempts", map);

        assertTrue(authUseCase.isBlocked("test@example.com"));
    }

}
