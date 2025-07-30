package com.pragma.usuarios.application.handler;


import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.mapper.UserMapper;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {
    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserMapper userMapper;

    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        userHandler = new UserHandler(userServicePort, userMapper);
    }

    @Test
    void saveOwner_shouldMapRequest_callService_andReturnResponse() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("owner@test.com");

        User userDomain = new User();
        userDomain.setEmail("owner@test.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("owner@test.com");

        CreateUserResponse response = new CreateUserResponse();

        when(userMapper.toUser(request)).thenReturn(userDomain);
        when(userServicePort.createOwner(eq(userDomain), any(User.class))).thenReturn(savedUser);
        when(userMapper.toUserResponse(savedUser)).thenReturn(response);

        // Act
        CreateUserResponse result = userHandler.saveOwner(request);

        // Assert
        assertNotNull(result);

        verify(userMapper).toUser(request);
        verify(userServicePort).createOwner(eq(userDomain), any(User.class));
        verify(userMapper).toUserResponse(savedUser);
    }
}
