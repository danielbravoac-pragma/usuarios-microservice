package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.User;

import java.util.List;

public interface IAuthServicePort {
    String getToken(User user);

    List<String> getRoles(User user);
}
