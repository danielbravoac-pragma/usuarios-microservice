package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserRolePersistencePort;
import com.pragma.usuarios.domain.usecase.UserRoleUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRoleUseCaseTest {
    @Mock
    private IUserRolePersistencePort userRolePersistencePort;

    private UserRoleUseCase userRoleUseCase;

    @BeforeEach
    void setUp() {
        userRoleUseCase = new UserRoleUseCase(userRolePersistencePort);
    }

    @Test
    void saveUserRole_shouldDelegateToPersistencePort() {
        User user = new User();
        user.setId(1L);
        Role role = new Role(UserRole.OWNER);

        userRoleUseCase.saveUserRole(user, role);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);

        verify(userRolePersistencePort, times(1)).saveUserRole(userCaptor.capture(), roleCaptor.capture());

        assertEquals(1L, userCaptor.getValue().getId());
        assertEquals(UserRole.OWNER, roleCaptor.getValue().getName());
    }

    @Test
    void findByUser_shouldReturnListStringOfRoles(){
        User user = new User();
        user.setId(1L);

        userRoleUseCase.findByUser(user);

        verify(userRolePersistencePort,times(1)).findByUser(user);

    }
}
