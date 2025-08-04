package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.User;

public interface IUserServicePort {
    User createUser(User user);

    User createOwner(User user);

    User createEmployee(User user);

    User findByEmail(String email);

    User findById(Long id);
}
