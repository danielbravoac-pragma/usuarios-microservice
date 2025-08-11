package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.AuthRequest;
import com.pragma.usuarios.application.dto.AuthResponse;
import com.pragma.usuarios.application.mapper.UserMapper;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthHandler implements IAuthHandler {

    private final IAuthServicePort authServicePort;
    private final UserMapper userMapper;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userMapper.toUserLogin(request);
        return new AuthResponse(authServicePort.getToken(user), authServicePort.getRoles(user));
    }
}
