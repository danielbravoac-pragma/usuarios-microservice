package com.pragma.usuarios.infrastructure.input.rest;


import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.dto.UserByIdResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "User", description = "Gestión de usuarios.")
public class UserRestController {

    private final IUserHandler userHandler;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/owner")
    @Operation(
            summary = "Crear Propietario",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Realiza el registro de un propietario, solo se puede acceder siendo administrador.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Propietario registrado con éxito",
                            content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "El Usuario no puede crear Propietarios",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                            content = @Content(schema = @Schema(implementation = Info.class)))

            }
    )
    public ResponseEntity<CreateUserResponse> saveOwner(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userHandler.saveOwner(createUserRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/employee")
    @Operation(
            summary = "Crear Empleado",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Realiza el registro de un empleado, solo se puede acceder siendo propietario.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Empleado registrado con éxito",
                            content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "El Usuario no puede crear Empleados",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                            content = @Content(schema = @Schema(implementation = Info.class)))

            }
    )
    public ResponseEntity<CreateUserResponse> saveEmployee(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userHandler.saveEmployee(createUserRequest), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Obtener Usuario",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Obtener Usuario por Id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empleado registrado con éxito",
                            content = @Content(schema = @Schema(implementation = UserByIdResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "500", description = "Error Interno",
                            content = @Content(schema = @Schema(implementation = Info.class)))

            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserByIdResponse> findById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(userHandler.findById(id), HttpStatus.OK);
    }

}
