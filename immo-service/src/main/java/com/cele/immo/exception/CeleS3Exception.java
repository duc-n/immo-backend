package com.cele.immo.exception;

public class CeleS3Exception extends RuntimeException {
    public CeleS3Exception(String message, Throwable throwable) {
        super(message, throwable);
    }
}
