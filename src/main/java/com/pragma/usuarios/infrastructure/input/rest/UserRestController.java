package com.pragma.usuarios.infrastructure.input.rest;


import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.handler.IUserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserHandler userHandler;

    @PostMapping("/owner")
    public ResponseEntity<CreateUserResponse> saveOwner(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userHandler.saveOwner(createUserRequest), HttpStatus.CREATED);
    }
}
