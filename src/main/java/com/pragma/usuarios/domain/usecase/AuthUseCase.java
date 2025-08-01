package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.application.exceptions.BlockedByLoginAttemptsException;
import com.pragma.usuarios.application.exceptions.InvalidCredentialsLoginException;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.api.IPasswordEncoderServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.output.jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private final JwtService jwtService;

    private final IUserServicePort userServicePort;

    private final IUserRoleServicePort userRoleServicePort;

    private final IPasswordEncoderServicePort passwordEncoderServicePort;

    private static final Integer MAX_ATTEMPTS = 5;
    private static final Integer LOCK_TIME_MINUTES = 15;

    @Data
    @AllArgsConstructor
    public static class LoginAttempt {
        private int attempts;
        private LocalDateTime lockUntil;
    }

    private final ConcurrentHashMap<String, LoginAttempt> attempts = new ConcurrentHashMap<>();


    @Override
    public String getToken(User user) {
        if (isBlocked(user.getEmail())) {
            throw new BlockedByLoginAttemptsException("Usuario bloqueado por reintentos fallidos");
        }

        User userLogged = userServicePort.findByEmail(user.getEmail());
        if (!passwordEncoderServicePort.matches(user.getPassword(), userLogged.getPassword())) {
            loginFailed(user.getEmail());
            throw new InvalidCredentialsLoginException("Credenciales inválidas.");
        }

        loginSucceeded(user.getEmail());

        List<String> roles = getRoles(userLogged);

        return jwtService.generateToken(userLogged.getId(), roles);
    }

    @Override
    public List<String> getRoles(User user) {
        User userLogged = userServicePort.findByEmail(user.getEmail());
        return userRoleServicePort.findByUser(userLogged);
    }

    @Override
    public void loginFailed(String email) {
        LoginAttempt attempt = attempts.getOrDefault(email, new LoginAttempt(0, null));
        int newAttempts = attempt.getAttempts() + 1;

        LocalDateTime lockUntil = null;
        if (newAttempts >= MAX_ATTEMPTS) {
            lockUntil = LocalDateTime.now().plusMinutes(LOCK_TIME_MINUTES);
        }

        attempts.put(email, new LoginAttempt(newAttempts, lockUntil));
    }

    @Override
    public void loginSucceeded(String email) {
        attempts.remove(email);
    }

    public boolean isBlocked(String email) {
        LoginAttempt attempt = attempts.get(email);
        if (attempt == null) return false;
        if (attempt.getLockUntil() == null) return false;
        return LocalDateTime.now().isBefore(attempt.getLockUntil());
    }
}
