package com.cele.immo.exception;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

abstract class DomainExceptionWithErrors extends GenericException implements HasDomainErrors {
    @Getter
    private final MapBindingResult bindingResult;

    DomainExceptionWithErrors(HttpStatus status, String reason) {
        this(status, reason, null, null);
    }

    DomainExceptionWithErrors(HttpStatus status, String reason, String objectName) {
        this(status, reason, objectName, null);
    }

    DomainExceptionWithErrors(HttpStatus status, String reason, String objectName, Seq<DomainError> errors) {
        super(status, reason);
        this.bindingResult = new MapBindingResult(new HashMap<>(), objectName);
        Option.of(errors).forEach(value -> value.forEach(this::addDomainError));
    }

}
