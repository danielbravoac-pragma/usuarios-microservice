package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.User;

public interface IUserServicePort {
    User createUser(User user);

    User createOwner(User user, User currentUser);

    User findByEmail(String email);

    User findById(Long id);
}
