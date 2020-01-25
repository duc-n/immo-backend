package com.cele.immo.exception;

import io.vavr.collection.Seq;

public class InvalidUserProfileException extends UnprocessableEntityException {
    public InvalidUserProfileException(String reason) {
        this(reason, null, null);
    }

    public InvalidUserProfileException(String reason, String objectName) {
        this(reason, objectName, null);
    }

    public InvalidUserProfileException(String reason, String objectName, Seq<DomainError> errors) {
        super(reason, objectName, errors);
    }
}
