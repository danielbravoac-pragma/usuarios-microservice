package com.pragma.usuarios.domain.api;

public interface IPasswordEncoderServicePort {
    String encode(String passwordStringClean);

    boolean matches(String passwordEncoded, String passwordUserLogged);
}
