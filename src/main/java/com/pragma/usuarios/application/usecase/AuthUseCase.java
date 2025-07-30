package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.application.exceptions.InvalidCredentialsLoginException;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.output.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private final JwtService jwtService;

    private final IUserServicePort userServicePort;

    private final IUserRoleServicePort userRoleServicePort;

    private final PasswordEncoder passwordEncoder;


    @Override
    public String getToken(User user) {
        User userLogged = userServicePort.findByEmail(user.getEmail());
        if (!passwordEncoder.matches(user.getPassword(), userLogged.getPassword())) {
            throw new InvalidCredentialsLoginException("Credenciales inválidas.");
        }

        List<String> roles = getRoles(userLogged);

        return jwtService.generateToken(userLogged.getId(), roles);
    }

    @Override
    public List<String> getRoles(User user) {
        User userLogged = userServicePort.findByEmail(user.getEmail());
        return userRoleServicePort.findByUser(userLogged);
    }
}
