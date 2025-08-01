package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.usecase.PermissionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PermissionUseCaseTest {
    @Test
    void canCreateOwner_shouldAlwaysReturnTrue() {
        PermissionUseCase permissionUseCase = new PermissionUseCase();

        User user = new User();
        user.setId(1L);

        boolean result = permissionUseCase.canCreateOwner(user);

        assertTrue(result, "El método canCreateOwner debería devolver true siempre");
    }
}
