package com.pragma.usuarios.application.usecase;


import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IRolePersistencePort;
import com.pragma.usuarios.domain.usecase.RoleUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseTest {
    @Mock
    private IRolePersistencePort rolePersistencePort;

    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
        roleUseCase = new RoleUseCase(rolePersistencePort);
    }

    @Test
    void findByName_shouldReturnRoleFromPersistencePort() {
        Role expectedRole = new Role(UserRole.ADMINISTRATOR);

        when(rolePersistencePort.findByName("ADMINISTRATOR")).thenReturn(expectedRole);

        Role result = roleUseCase.findByName("ADMINISTRATOR");

        assertNotNull(result);
        assertEquals(UserRole.ADMINISTRATOR, result.getName());
        verify(rolePersistencePort, times(1)).findByName("ADMINISTRATOR");
    }
}
