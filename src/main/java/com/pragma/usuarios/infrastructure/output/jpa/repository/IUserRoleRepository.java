package com.pragma.usuarios.infrastructure.output.jpa.repository;

import com.pragma.usuarios.infrastructure.output.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UserRoleEntity;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UserRoleId;

import java.util.List;

public interface IUserRoleRepository extends IGenericRepository<UserRoleEntity, UserRoleId> {
    List<UserRoleEntity> findByUser(UserEntity userEntity);
}
