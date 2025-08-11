package com.pragma.usuarios.infrastructure.input.rest;


import com.pragma.usuarios.application.dto.AuthRequest;
import com.pragma.usuarios.application.dto.AuthResponse;
import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.handler.IAuthHandler;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.infrastructure.exceptionhandler.Info;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "Gestión de autenticación.")
public class AuthRestController {

    private final IAuthHandler authHandler;

    private final IUserHandler userHandler;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar Sesion",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Iniciar Sesion con credenciales.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Sesión iniciada correctamente",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciales Incorrectas",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "500", description = "Error Interno")

            }
    )
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        return new ResponseEntity<>(authHandler.login(authRequest), HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Registrar Cliente",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Registrar cliente autoatencion.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario Registrado Correctamente",
                            content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "500", description = "Error Interno")

            }
    )
    public ResponseEntity<CreateUserResponse> register(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userHandler.saveCustomer(createUserRequest), HttpStatus.CREATED);
    }
}
