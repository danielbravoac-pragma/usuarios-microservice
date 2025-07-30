package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;

public interface IUserRolePersistencePort {
    void saveUserRole(User user, Role role);
}
