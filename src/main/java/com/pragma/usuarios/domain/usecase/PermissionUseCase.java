package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.application.exceptions.AccessDeniedException;
import com.pragma.usuarios.domain.api.IPermissionServicePort;
import com.pragma.usuarios.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RequiredArgsConstructor
public class PermissionUseCase implements IPermissionServicePort {

    @Override
    public boolean canCreateOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (!roles.contains("ROLE_" + UserRole.ADMINISTRATOR.toString())) {
            throw new AccessDeniedException("Solo el ADMINISTRADOR puede crear un Propietario");
        }

        return true;
    }

    @Override
    public void canCreateEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (!roles.contains("ROLE_" + UserRole.OWNER.toString())) {
            throw new AccessDeniedException("Solo el PROPIETARIO puede crear un Empleado");
        }

    }
}
