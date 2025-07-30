package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.User;

public interface IPermissionServicePort {
    boolean canCreateOwner(User user);
}
