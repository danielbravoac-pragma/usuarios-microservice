package com.pragma.usuarios.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreateUserResponse {
    private String name;
    private String lastName;
    private String email;
    private String documentNumber;
    private String role;
}
