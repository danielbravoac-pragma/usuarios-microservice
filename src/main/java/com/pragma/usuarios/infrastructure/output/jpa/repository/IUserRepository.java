package com.pragma.usuarios.infrastructure.output.jpa.repository;

import com.pragma.usuarios.infrastructure.output.jpa.entity.UserEntity;

import java.util.Optional;

public interface IUserRepository extends IGenericRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
