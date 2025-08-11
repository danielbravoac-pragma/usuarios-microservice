package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IPasswordEncoderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PasswordUseCase implements IPasswordEncoderServicePort {

    private final PasswordEncoder passwordEncoder;


    @Override
    public String encode(String passwordStringClean) {
        return passwordEncoder.encode(passwordStringClean);
    }

    @Override
    public boolean matches(String passwordEncoded, String passwordUserLogged) {
        return passwordEncoder.matches(passwordEncoded, passwordUserLogged);
    }
}
