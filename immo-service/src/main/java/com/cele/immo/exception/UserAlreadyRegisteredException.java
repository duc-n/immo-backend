package com.cele.immo.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyRegisteredException extends GenericException {
    UserAlreadyRegisteredException(HttpStatus status, String message) {
        super(status, message);
    }
}
