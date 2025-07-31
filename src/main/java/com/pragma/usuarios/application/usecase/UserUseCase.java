package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.application.exceptions.AccessDeniedException;
import com.pragma.usuarios.application.exceptions.DataNotExistsException;
import com.pragma.usuarios.application.exceptions.InvalidAgeException;
import com.pragma.usuarios.application.exceptions.UserAlreadyRegisteredException;
import com.pragma.usuarios.domain.api.IPermissionServicePort;
import com.pragma.usuarios.domain.api.IRoleServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    private final PasswordEncoder passwordEncoder;

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

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userPersistencePort.saveUser(user);

        user.getRoles().forEach(roleRequest -> {
            Role roleComplete = roleServicePort.findByName(roleRequest.getName().toString());
            userRoleServicePort.saveUserRole(savedUser, roleComplete);
        });

        return savedUser;
    }

    @Override
    public User createOwner(User user, User currentUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (!roles.contains("ROLE_" + UserRole.ADMINISTRATOR.toString())) {
            throw new AccessDeniedException("Solo el ADMINISTRADOR puede crear un Propietario");
        }

        permissionServicePort.canCreateOwner(currentUser);
        user.getRoles().add(new Role(UserRole.OWNER));
        return createUser(user);
    }

    @Override
    public User findByEmail(String email) {
        return userPersistencePort.findByEmail(email)
                .orElseThrow(() -> new DataNotExistsException("User not registered."));
    }

    @Override
    public User findById(Long id) {
        return userPersistencePort.findById(id);
    }

}
