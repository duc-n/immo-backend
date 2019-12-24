package com.cele.immo.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString(callSuper = true)
abstract class GenericException extends RuntimeException {
    @Getter
    private final HttpStatus status;

    GenericException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    GenericException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
