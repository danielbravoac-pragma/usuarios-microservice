package com.pragma.usuarios.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud de Autenticación")
public class AuthRequest {
    @NotBlank
    @Email
    @Schema(description = "Email de usuario",example = "mail@mail.com")
    private String email;

    @NotBlank
    @Schema(description = "Contraseña de usuario",example = "12345679")
    private String password;
}
