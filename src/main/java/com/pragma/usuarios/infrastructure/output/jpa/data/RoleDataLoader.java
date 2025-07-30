package com.pragma.usuarios.infrastructure.output.jpa.data;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataLoader implements CommandLineRunner {

    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(roleEntityMapper.toRoleEntity(new Role(UserRole.ADMINISTRATOR)));
            roleRepository.save(roleEntityMapper.toRoleEntity(new Role(UserRole.OWNER)));
            roleRepository.save(roleEntityMapper.toRoleEntity(new Role(UserRole.EMPLOYEE)));
            roleRepository.save(roleEntityMapper.toRoleEntity(new Role(UserRole.CUSTOMER)));
        }
    }
}
