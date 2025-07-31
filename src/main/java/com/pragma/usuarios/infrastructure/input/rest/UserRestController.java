package com.pragma.usuarios.infrastructure.input.rest;


import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.dto.UserByIdResponse;
import com.pragma.usuarios.application.handler.IUserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserHandler userHandler;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/owner")
    public ResponseEntity<CreateUserResponse> saveOwner(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userHandler.saveOwner(createUserRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserByIdResponse> findById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(userHandler.findById(id), HttpStatus.OK);
    }

}
