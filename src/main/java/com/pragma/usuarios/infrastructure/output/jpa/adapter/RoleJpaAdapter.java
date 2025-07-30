package com.pragma.usuarios.infrastructure.output.jpa.adapter;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.spi.IRolePersistencePort;
import com.pragma.usuarios.infrastructure.exception.DataNotFoundException;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;

    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public Role findByName(String name) {
        return roleEntityMapper.toRole(roleRepository.findByName(name).orElseThrow(() -> new DataNotFoundException("Role not found")));
    }
}
