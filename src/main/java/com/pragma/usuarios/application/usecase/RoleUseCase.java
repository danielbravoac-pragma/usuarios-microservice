package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.domain.api.IRoleServicePort;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.spi.IRolePersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleUseCase implements IRoleServicePort {

    private final IRolePersistencePort rolePersistencePort;

    @Override
    public Role findByName(String name) {
        return rolePersistencePort.findByName(name);
    }
}
