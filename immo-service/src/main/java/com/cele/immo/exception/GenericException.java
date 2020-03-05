package com.cele.immo.exception;

import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ToString(callSuper = true)
abstract class GenericException extends ResponseStatusException {

    GenericException(HttpStatus status, String message) {
        super(status, message);
    }


}
