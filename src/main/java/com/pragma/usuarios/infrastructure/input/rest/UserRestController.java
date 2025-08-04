package com.pragma.usuarios.infrastructure.input.rest;


import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.dto.UserByIdResponse;
import com.pragma.usuarios.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserHandler userHandler;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/owner")
    @Operation(
            summary = "Crear Propietario",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Realiza el registro de un propietario, solo se puede acceder siendo administrador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Propietario registrado con éxito",
                            content = @Content(schema = @Schema(implementation = CreateUserRequest.class))),

            }
    )
    public ResponseEntity<CreateUserResponse> saveOwner(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userHandler.saveOwner(createUserRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/employee")
    public ResponseEntity<CreateUserResponse> saveEmployee(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userHandler.saveEmployee(createUserRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserByIdResponse> findById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(userHandler.findById(id), HttpStatus.OK);
    }

}
