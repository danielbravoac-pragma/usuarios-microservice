package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;

public interface IUserHandler {
    CreateUserResponse saveOwner(CreateUserRequest createUserRequest);
}
