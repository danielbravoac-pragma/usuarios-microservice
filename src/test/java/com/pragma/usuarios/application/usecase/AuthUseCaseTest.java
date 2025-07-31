package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.application.exceptions.InvalidCredentialsLoginException;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.output.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private IUserServicePort userServicePort;
    @Mock
    private IUserRoleServicePort userRoleServicePort;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User loginRequest;
    private User userInDb;

    @BeforeEach
    void setUp() {
        loginRequest = new User();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        userInDb = new User();
        userInDb.setId(1L);
        userInDb.setEmail("test@test.com");
        userInDb.setPassword("hashed-pass");
    }

    @Test
    void getToken_ShouldReturnToken_WhenCredentialsValid() {
        when(userServicePort.findByEmail("test@test.com")).thenReturn(userInDb);
        when(passwordEncoder.matches("1234", "hashed-pass")).thenReturn(true);
        when(userRoleServicePort.findByUser(userInDb)).thenReturn(List.of("OWNER"));
        when(jwtService.generateToken(1L, List.of("OWNER"))).thenReturn("fake-jwt-token");

        String token = authUseCase.getToken(loginRequest);

        assertEquals("fake-jwt-token", token);
    }

    @Test
    void getToken_ShouldThrowException_WhenPasswordInvalid() {
        when(userServicePort.findByEmail("test@test.com")).thenReturn(userInDb);
        when(passwordEncoder.matches("1234", "hashed-pass")).thenReturn(false);

        assertThrows(InvalidCredentialsLoginException.class,
                () -> authUseCase.getToken(loginRequest));
    }

    @Test
    void getRoles_ShouldReturnRoles() {
        when(userServicePort.findByEmail("test@test.com")).thenReturn(userInDb);
        when(userRoleServicePort.findByUser(userInDb)).thenReturn(List.of("ADMINISTRATOR", "OWNER"));

        List<String> roles = authUseCase.getRoles(loginRequest);

        assertEquals(List.of("ADMINISTRATOR", "OWNER"), roles);
    }
}
