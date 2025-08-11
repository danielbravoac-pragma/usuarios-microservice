package com.pragma.usuarios.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Creación de usuario")
public class CreateUserRequest {

    @NotBlank
    @Schema(description = "Nombre de usuario", example = "John")
    private String name;

    @NotBlank
    @Schema(description = "Apellido de usuario", example = "Doe")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "El número de documento debe contener únicamente números.")
    @Schema(description = "Numero de Documento", example = "88523614815")
    private String documentNumber;

    @NotBlank
    @Size(min = 10, max = 13)
    @Pattern(regexp = "^\\+\\d{9,12}$", message = "El formato del teléfono no es válido. Debe empezar con '+' seguido de 9 a 12 dígitos (ej: +570000000000).")
    @Schema(description = "Numero de telefono", example = "+578529631234")
    private String phoneNumber;

    @NotNull
    @Past
    @Schema(description = "Fecha de Nacimiento")
    private LocalDate birthDate;

    @NotEmpty
    @Email
    @Schema(description = "Email de usuario", example = "mail@mail.com")
    private String email;

    @NotEmpty
    @Schema(description = "Contraseña de usuario", example = "12345679")
    private String password;

    @Schema(description = "Id de Restaurant", example = "1")
    private Long restaurantId;
}
