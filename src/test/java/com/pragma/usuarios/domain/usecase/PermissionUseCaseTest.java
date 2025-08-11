package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.application.exceptions.AccessDeniedException;
import com.pragma.usuarios.domain.model.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PermissionUseCaseTest {

    private PermissionUseCase permissionUseCase;

    @BeforeEach
    void setUp() {
        permissionUseCase = new PermissionUseCase();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void canCreateOwner_shouldReturnTrue_whenUserIsAdministrator() {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + UserRole.ADMINISTRATOR)
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("adminUser", null, authorities)
        );

        boolean result = permissionUseCase.canCreateOwner();

        assertTrue(result);
    }

    @Test
    void canCreateOwner_shouldThrowAccessDenied_whenUserIsNotAdministrator() {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + UserRole.OWNER)
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("ownerUser", null, authorities)
        );

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () ->
                permissionUseCase.canCreateOwner()
        );

        assertEquals("Solo el ADMINISTRADOR puede crear un Propietario", exception.getMessage());
    }

    @Test
    void canCreateEmployee_shouldNotThrow_whenUserIsOwner() {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + UserRole.OWNER)
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("ownerUser", null, authorities)
        );

        assertDoesNotThrow(() -> permissionUseCase.canCreateEmployee());
    }

    @Test
    void canCreateEmployee_shouldThrowAccessDenied_whenUserIsNotOwner() {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + UserRole.ADMINISTRATOR)
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("adminUser", null, authorities)
        );

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () ->
                permissionUseCase.canCreateEmployee()
        );

        assertEquals("Solo el PROPIETARIO puede crear un Empleado", exception.getMessage());
    }
}
