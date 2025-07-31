package com.pragma.usuarios.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private String name;
    private String lastName;
    private String email;
    private String documentNumber;
    private String role;
}
