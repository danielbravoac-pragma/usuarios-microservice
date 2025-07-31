package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.dto.UserByIdResponse;

public interface IUserHandler {
    CreateUserResponse saveOwner(CreateUserRequest createUserRequest);

    UserByIdResponse findById(Long id);
}
