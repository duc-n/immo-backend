package com.cele.immo.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GenericException {
    public UserNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
