package com.cele.immo.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class UnprocessableEntityException extends GenericException {
    UnprocessableEntityException(String message) {
        super(UNPROCESSABLE_ENTITY, message);
    }

    UnprocessableEntityException(String message, Throwable cause) {
        super(UNPROCESSABLE_ENTITY, message, cause);
    }
}
