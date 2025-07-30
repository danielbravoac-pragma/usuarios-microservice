package com.pragma.usuarios.infrastructure.output.jpa.adapter;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.spi.IUserRolePersistencePort;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UserRoleEntity;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UserRoleId;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IUserRoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRoleJpaAdapter implements IUserRolePersistencePort {

    private final IUserRoleRepository userRoleRepository;

    private final IUserEntityMapper userEntityMapper;

    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public void saveUserRole(User user, Role role) {
        UserRoleId id = new UserRoleId(user.getId(), role.getId());
        UserRoleEntity relation = new UserRoleEntity();
        relation.setId(id);
        relation.setUser(userEntityMapper.toUserEntity(user));
        relation.setRole(roleEntityMapper.toRoleEntity(role));
        userRoleRepository.save(relation);
    }
}
