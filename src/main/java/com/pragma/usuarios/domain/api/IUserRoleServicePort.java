package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;

import java.util.List;

public interface IUserRoleServicePort {
    void saveUserRole(User user, Role role);

    List<String> findByUser(User user);

}
