package com.pragma.usuarios.application.exceptions;

public class InvalidCredentialsLoginException extends RuntimeException {
    public InvalidCredentialsLoginException(String message) {
        super(message);
    }
}
