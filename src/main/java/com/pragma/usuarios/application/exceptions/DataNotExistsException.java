package com.pragma.usuarios.application.exceptions;

public class DataNotExistsException extends RuntimeException {
    public DataNotExistsException(String message) {
        super(message);
    }
}
