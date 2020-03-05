package com.cele.immo.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyRegisteredException extends GenericException {
    public UserAlreadyRegisteredException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
