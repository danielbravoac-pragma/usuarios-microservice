package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.Role;

public interface IRoleServicePort {
    Role findByName(String name);
}
