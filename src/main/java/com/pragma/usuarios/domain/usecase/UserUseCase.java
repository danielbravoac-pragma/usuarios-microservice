package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.application.exceptions.DataNotExistsException;
import com.pragma.usuarios.application.exceptions.InvalidAgeException;
import com.pragma.usuarios.application.exceptions.UserAlreadyRegisteredException;
import com.pragma.usuarios.domain.api.*;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    private final IPasswordEncoderServicePort passwordEncoderServicePort;

    private final IPermissionServicePort permissionServicePort;

    private final IRoleServicePort roleServicePort;

    private final IUserRoleServicePort userRoleServicePort;

    @Override
    public User createUser(User user) {
        if (Period.between(user.getBirthDate(), LocalDate.now()).getYears() < 18) {
            throw new InvalidAgeException("El usuario debe ser mayor de edad.");
        }

        userPersistencePort.findByEmail(user.getEmail()).ifPresent(
                u -> {
                    throw new UserAlreadyRegisteredException("El email ya está registrado.");
                }
        );

        user.setPassword(passwordEncoderServicePort.encode(user.getPassword()));

        User savedUser = userPersistencePort.saveUser(user);

        user.getRoles().forEach(roleRequest -> {
            Role roleComplete = roleServicePort.findByName(roleRequest.getName().toString());
            userRoleServicePort.saveUserRole(savedUser, roleComplete);
        });

        return savedUser;
    }

    @Override
    public User createOwner(User user) {
        permissionServicePort.canCreateOwner();
        user.getRoles().add(new Role(UserRole.OWNER));
        return createUser(user);
    }

    @Override
    public User createEmployee(User user) {
        permissionServicePort.canCreateEmployee();
        user.getRoles().add(new Role(UserRole.EMPLOYEE));
        return createUser(user);
    }

    @Override
    public User findByEmail(String email) {
        return userPersistencePort.findByEmail(email)
                .orElseThrow(() -> new DataNotExistsException("User not registered."));
    }

    @Override
    public User createClient(User user) {
        user.getRoles().add(new Role(UserRole.CUSTOMER));
        return createUser(user);
    }

    @Override
    public User findById(Long id) {
        return userPersistencePort.findById(id);
    }

}
