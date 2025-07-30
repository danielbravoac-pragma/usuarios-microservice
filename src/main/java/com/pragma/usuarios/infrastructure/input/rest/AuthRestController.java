package com.pragma.usuarios.infrastructure.input.rest;


import com.pragma.usuarios.application.dto.AuthRequest;
import com.pragma.usuarios.application.dto.AuthResponse;
import com.pragma.usuarios.application.handler.IAuthHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final IAuthHandler authHandler;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        return new ResponseEntity<>(authHandler.login(authRequest), HttpStatus.ACCEPTED);
    }
}
