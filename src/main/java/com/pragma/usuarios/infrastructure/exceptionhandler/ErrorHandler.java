package com.pragma.usuarios.infrastructure.exceptionhandler;

import com.pragma.usuarios.application.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleDefaultException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SOMETHING HAPPEN - CONTACT THE IT TEAM")), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BlockedByLoginAttemptsException.class)
    public ResponseEntity<GenericResponse> handleBlockedByLoginAttemptsException(BlockedByLoginAttemptsException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.UNAUTHORIZED.value(), "USER BLOCKED BY RETRYING FAILED LOGIN -- TRY AGAIN LATER")), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining(","));
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), "SOME INVALID FIELDS", "BAD REQUEST", message)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.pragma.usuarios.application.exceptions.AccessDeniedException.class)
    public ResponseEntity<GenericResponse> handleAccessDeniedException(com.pragma.usuarios.application.exceptions.AccessDeniedException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED TO MAKE THIS OPERATION")), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataNotExistsException.class)
    public ResponseEntity<GenericResponse> handleDataNotFoundException(DataNotExistsException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.NOT_FOUND.value(), "DATA NOT FOUND")), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<GenericResponse> handleInvalidAgeException(InvalidAgeException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), "USER MUST BE AN ADULT.")), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<GenericResponse> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.UNAUTHORIZED.value(), "DON'T HAVE PERMISSION TO ACCESS THIS OPERATION CHECK YOUR SESSION")), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), "CREDENTIALS ARE INCORRECT")), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsLoginException.class)
    public ResponseEntity<GenericResponse> handleInvalidCredentialsLoginException(InvalidCredentialsLoginException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), "CREDENTIALS ARE INCORRECT")), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<GenericResponse> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), "USER ALREADY EXISTS.")), HttpStatus.BAD_REQUEST);
    }

}
