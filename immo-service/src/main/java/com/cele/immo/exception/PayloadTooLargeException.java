package com.cele.immo.exception;

import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;

public class PayloadTooLargeException extends GenericException {

    PayloadTooLargeException(String message) {
        super(PAYLOAD_TOO_LARGE, message);
    }

    PayloadTooLargeException(String message, Throwable cause) {
        super(PAYLOAD_TOO_LARGE, message, cause);
    }
}
