package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.handler.IUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRestControllerTest {
    private IUserHandler userHandler;
    private UserRestController userRestController;

    @BeforeEach
    void setUp() {
        userHandler = mock(IUserHandler.class);
        userRestController = new UserRestController(userHandler);
    }

    @Test
    void saveOwner_shouldReturn201_andResponse() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("john@test.com");

        CreateUserResponse response = new CreateUserResponse();
        response.setEmail("john@test.com");

        when(userHandler.saveOwner(any(CreateUserRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<CreateUserResponse> result = userRestController.saveOwner(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("john@test.com", result.getBody().getEmail());

        verify(userHandler, times(1)).saveOwner(request);
    }
}
