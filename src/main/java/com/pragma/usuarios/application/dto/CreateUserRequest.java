package com.pragma.usuarios.application.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "El número de documento debe contener únicamente números.")
    private String documentNumber;

    @NotBlank
    @Size(min = 10, max = 13)
    @Pattern(regexp = "^\\+\\d{9,12}$", message = "El formato del teléfono no es válido. Debe empezar con '+' seguido de 9 a 12 dígitos (ej: +570000000000).")
    private String phoneNumber;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;
}
