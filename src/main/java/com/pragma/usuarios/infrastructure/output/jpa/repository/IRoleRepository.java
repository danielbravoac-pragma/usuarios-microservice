package com.pragma.usuarios.infrastructure.output.jpa.repository;

import com.pragma.usuarios.infrastructure.output.jpa.entity.RoleEntity;

import java.util.Optional;

public interface IRoleRepository extends IGenericRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
