package com.pragma.usuarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Long id;

    public Role(UserRole name) {
        this.name = name;
    }

    private UserRole name; //Propietario - Administrador - Trabajador - Cliente
}
