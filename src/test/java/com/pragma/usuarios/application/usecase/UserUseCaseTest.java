package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.application.exceptions.AccessDeniedException;
import com.pragma.usuarios.application.exceptions.DataNotExistsException;
import com.pragma.usuarios.application.exceptions.InvalidAgeException;
import com.pragma.usuarios.application.exceptions.UserAlreadyRegisteredException;
import com.pragma.usuarios.domain.api.IPasswordEncoderServicePort;
import com.pragma.usuarios.domain.api.IPermissionServicePort;
import com.pragma.usuarios.domain.api.IRoleServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.domain.usecase.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IPasswordEncoderServicePort passwordEncoder;
    @Mock
    private IPermissionServicePort permissionServicePort;
    @Mock
    private IRoleServicePort roleServicePort;
    @Mock
    private IUserRoleServicePort userRoleServicePort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Daniel");
        user.setEmail("test@test.com");
        user.setPassword("1234");
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setRoles(new HashSet<>(Set.of(new Role(UserRole.CUSTOMER))));
    }

    @Test
    void createUser_ShouldSaveUser_WhenValid() {
        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);
        when(roleServicePort.findByName(any())).thenReturn(new Role(UserRole.CUSTOMER));

        User result = userUseCase.createUser(user);

        assertEquals(user.getEmail(), result.getEmail());
        verify(userPersistencePort).saveUser(any(User.class));
        verify(userRoleServicePort).saveUserRole(any(User.class), any(Role.class));
    }

    @Test
    void createUser_ShouldThrowInvalidAgeException_WhenUnder18() {
        user.setBirthDate(LocalDate.now().minusYears(16));
        assertThrows(InvalidAgeException.class, () -> userUseCase.createUser(user));
    }

    @Test
    void createUser_ShouldThrowUserAlreadyRegisteredException_WhenEmailExists() {
        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyRegisteredException.class, () -> userUseCase.createUser(user));
    }

    @Test
    void findByEmail_ShouldThrowDataNotExistsException_WhenUserByEmailNotExists() {
        when(userPersistencePort.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(DataNotExistsException.class, () -> userUseCase.findByEmail("mail@mail.com"));
    }

    @Test
    void createOwner_ShouldCreateOwner_WhenUserIsAdmin() {
        // Simular que el usuario actual tiene ROLE_ADMINISTRATOR
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null,
                        List.of(() -> "ROLE_ADMINISTRATOR"))
        );

        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);
        when(roleServicePort.findByName(any())).thenReturn(new Role(UserRole.OWNER));

        User result = userUseCase.createOwner(user);

        assertTrue(result.getRoles().stream().anyMatch(r -> r.getName() == UserRole.OWNER));
        verify(permissionServicePort).canCreateOwner();
    }

    @Test
    void createOwner_ShouldThrowAccessDenied_WhenNotAdmin() {
        // Simular usuario sin rol administrador
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null,
                        List.of(() -> "ROLE_CUSTOMER"))
        );

        assertThrows(AccessDeniedException.class, () -> userUseCase.createOwner(user));
        verify(permissionServicePort, never()).canCreateOwner();
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        when(userPersistencePort.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        User result = userUseCase.findByEmail("test@test.com");
        assertEquals("Daniel", result.getName());
    }

    @Test
    void findById_ShouldReturnUser() {
        when(userPersistencePort.findById(1L)).thenReturn(user);
        User result = userUseCase.findById(1L);
        assertEquals(1L, result.getId());
    }
}
