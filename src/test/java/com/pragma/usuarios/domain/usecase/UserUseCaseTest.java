package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.application.exceptions.DataNotExistsException;
import com.pragma.usuarios.application.exceptions.InvalidAgeException;
import com.pragma.usuarios.application.exceptions.UserAlreadyRegisteredException;
import com.pragma.usuarios.domain.api.*;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock private IUserPersistencePort userPersistencePort;
    @Mock private IPasswordEncoderServicePort passwordEncoderServicePort;
    @Mock private IPermissionServicePort permissionServicePort;
    @Mock private IRoleServicePort roleServicePort;
    @Mock private IUserRoleServicePort userRoleServicePort;

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userUseCase = new UserUseCase(
                userPersistencePort,
                passwordEncoderServicePort,
                permissionServicePort,
                roleServicePort,
                userRoleServicePort
        );
    }

    @Test
    void createUser_shouldThrowInvalidAgeException_ifUnder18() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(17));
        user.setEmail("test@example.com");
        user.setPassword("123456");

        assertThrows(InvalidAgeException.class, () -> userUseCase.createUser(user));
        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createUser_shouldThrowUserAlreadyRegisteredException_ifEmailExists() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setEmail("duplicate@example.com");
        user.setPassword("123456");

        when(userPersistencePort.findByEmail("duplicate@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyRegisteredException.class, () -> userUseCase.createUser(user));
        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createUser_shouldSaveUserAndAssignRoles() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(25));
        user.setEmail("newuser@example.com");
        user.setPassword("rawpass");
        user.setRoles(new HashSet<>(List.of(new Role(UserRole.CUSTOMER))));

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("newuser@example.com");

        Role fullRole = new Role(UserRole.CUSTOMER);

        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoderServicePort.encode("rawpass")).thenReturn("hashedpass");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(savedUser);
        when(roleServicePort.findByName("CUSTOMER")).thenReturn(fullRole);

        User result = userUseCase.createUser(user);

        assertEquals(savedUser.getEmail(), result.getEmail());
        verify(userPersistencePort).saveUser(any(User.class));
        verify(userRoleServicePort).saveUserRole(savedUser, fullRole);
    }

    @Test
    void createOwner_shouldCheckPermissionAndAddRole() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setEmail("owner@example.com");
        user.setPassword("pass");
        user.setRoles(new HashSet<>());

        User savedUser = new User();
        savedUser.setEmail("owner@example.com");

        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoderServicePort.encode("pass")).thenReturn("encoded");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(savedUser);
        when(roleServicePort.findByName("OWNER")).thenReturn(new Role(UserRole.OWNER));

        User result = userUseCase.createOwner(user);

        assertEquals("owner@example.com", result.getEmail());
        verify(permissionServicePort).canCreateOwner();
    }

    @Test
    void createEmployee_shouldCheckPermissionAndAddRole() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(21));
        user.setEmail("employee@example.com");
        user.setPassword("pass");
        user.setRoles(new HashSet<>());

        User savedUser = new User();
        savedUser.setEmail("employee@example.com");

        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoderServicePort.encode("pass")).thenReturn("encoded");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(savedUser);
        when(roleServicePort.findByName("EMPLOYEE")).thenReturn(new Role(UserRole.EMPLOYEE));

        User result = userUseCase.createEmployee(user);

        assertEquals("employee@example.com", result.getEmail());
        verify(permissionServicePort).canCreateEmployee();
    }

    @Test
    void createClient_shouldAddCustomerRoleAndCreateUser() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(30));
        user.setEmail("client@example.com");
        user.setPassword("pass");
        user.setRoles(new HashSet<>());

        User savedUser = new User();
        savedUser.setEmail("client@example.com");

        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoderServicePort.encode("pass")).thenReturn("encoded");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(savedUser);
        when(roleServicePort.findByName("CUSTOMER")).thenReturn(new Role(UserRole.CUSTOMER));

        User result = userUseCase.createClient(user);

        assertEquals("client@example.com", result.getEmail());
        verify(userPersistencePort).saveUser(any(User.class));
    }

    @Test
    void findByEmail_shouldReturnUserIfExists() {
        String email = "found@example.com";
        User user = new User();
        user.setEmail(email);

        when(userPersistencePort.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userUseCase.findByEmail(email);

        assertEquals(email, result.getEmail());
    }

    @Test
    void findByEmail_shouldThrowIfNotFound() {
        String email = "missing@example.com";

        when(userPersistencePort.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(DataNotExistsException.class, () -> userUseCase.findByEmail(email));
    }

    @Test
    void findById_shouldReturnUser() {
        User user = new User();
        user.setId(55L);

        when(userPersistencePort.findById(55L)).thenReturn(user);

        User result = userUseCase.findById(55L);

        assertEquals(55L, result.getId());
        verify(userPersistencePort).findById(55L);
    }
}
