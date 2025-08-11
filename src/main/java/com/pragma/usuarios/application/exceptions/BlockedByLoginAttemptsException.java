package com.pragma.usuarios.application.exceptions;

public class BlockedByLoginAttemptsException extends RuntimeException {
    public BlockedByLoginAttemptsException(String message) {
        super(message);
    }
}
