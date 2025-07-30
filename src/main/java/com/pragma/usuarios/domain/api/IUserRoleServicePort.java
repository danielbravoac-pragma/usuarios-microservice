package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;

public interface IUserRoleServicePort {
    void saveUserRole(User user, Role role);
}
