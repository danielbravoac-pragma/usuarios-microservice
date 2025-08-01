package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IPermissionServicePort;
import com.pragma.usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PermissionUseCase implements IPermissionServicePort {

    @Override
    public boolean canCreateOwner(User user) {
        return true;
    }
}
