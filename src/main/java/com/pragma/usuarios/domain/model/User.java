package com.pragma.usuarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String lastName;
    private String documentNumber;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
}
