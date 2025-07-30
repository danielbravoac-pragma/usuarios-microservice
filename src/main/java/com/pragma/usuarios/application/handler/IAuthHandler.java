package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.AuthRequest;
import com.pragma.usuarios.application.dto.AuthResponse;

public interface IAuthHandler {
    AuthResponse login(AuthRequest request);
}
