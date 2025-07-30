package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.spi.IUserRolePersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRoleUseCase implements IUserRoleServicePort {

    private final IUserRolePersistencePort userRolePersistencePort;

    @Override
    public void saveUserRole(User user, Role role) {
        userRolePersistencePort.saveUserRole(user, role);
    }

    @Override
    public List<String> findByUser(User user) {
        return userRolePersistencePort.findByUser(user);
    }
}
