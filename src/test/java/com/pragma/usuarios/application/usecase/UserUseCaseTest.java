package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.application.exceptions.InvalidAgeException;
import com.pragma.usuarios.application.exceptions.UserAlreadyRegisteredException;
import com.pragma.usuarios.data.UserTestData;
import com.pragma.usuarios.domain.api.IPermissionServicePort;
import com.pragma.usuarios.domain.api.IRoleServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IPermissionServicePort permissionServicePort;
    @Mock
    private IRoleServicePort roleServicePort;
    @Mock
    private IUserRoleServicePort userRoleServicePort;

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userPersistencePort, passwordEncoder, permissionServicePort, roleServicePort, userRoleServicePort);
    }

    @Test
    void createUser_shouldThrowInvalidAgeException_whenUserIsMinor() {
        User user = UserTestData.underageUser();

        assertThrows(InvalidAgeException.class, () -> userUseCase.createUser(user));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createUser_shouldThrowUserAlreadyRegisteredException_whenEmailExists() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setEmail("test@mail.com");

        when(userPersistencePort.findByEmail("test@mail.com")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyRegisteredException.class, () -> userUseCase.createUser(user));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createUser_shouldEncryptPassword_andSaveUser_andAssignRoles() {
        User user = UserTestData.validUser();
        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);


        assertEquals("hashedPassword", user.getPassword());
        verify(userPersistencePort).saveUser(any(User.class));
    }

    @Test
    void createOwner_shouldCallPermissionService_andAssignOwnerRole() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(22));
        user.setEmail("owner@test.com");

        User currentUser = new User();
        currentUser.setId(99L);

        User savedUser = new User();
        savedUser.setId(1L);

        when(userPersistencePort.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userPersistencePort.saveUser(any())).thenReturn(savedUser);
        when(roleServicePort.findByName("OWNER")).thenReturn(new Role(UserRole.OWNER));

        userUseCase.createOwner(user, currentUser);

        verify(permissionServicePort).canCreateOwner(currentUser);
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName() == UserRole.OWNER));
    }
}
