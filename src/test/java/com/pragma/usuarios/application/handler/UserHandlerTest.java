package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.dto.UserByIdResponse;
import com.pragma.usuarios.application.mapper.UserMapper;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
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

class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserMapper userMapper;

    @Mock
    private IAuthServicePort authServicePort;

    @InjectMocks
    private UserHandler userHandler;

    private CreateUserRequest createUserRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setName("Daniel");
        createUserRequest.setLastName("Bravo");
        createUserRequest.setEmail("test@test.com");
        createUserRequest.setPassword("1234");

        user = new User();
        user.setId(1L);
        user.setName("Daniel");
    }

    @Test
    void saveOwner_ShouldReturnCreateUserResponse() {
        // Arrange
        CreateUserResponse expectedResponse = new CreateUserResponse();
        expectedResponse.setName("Daniel");

        when(userMapper.toUser(createUserRequest)).thenReturn(user);
        when(userServicePort.createOwner(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(expectedResponse);

        // Act
        CreateUserResponse response = userHandler.saveOwner(createUserRequest);

        // Assert
        assertEquals("Daniel", response.getName());
    }

    @Test
    void findById_ShouldReturnUserByIdResponse() {
        // Arrange
        when(userServicePort.findById(1L)).thenReturn(user);
        when(authServicePort.getRoles(user)).thenReturn(List.of("OWNER"));

        // Act
        UserByIdResponse response = userHandler.findById(1L);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Daniel", response.getName());
        assertEquals(List.of("OWNER"), response.getRoles());
    }
}
