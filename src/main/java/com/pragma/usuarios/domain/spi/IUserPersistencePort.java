package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {
    User saveUser(User user);

    Optional<User> findByEmail(String email);

    User findById(Long id);
}
