package com.allanaoliveira.demo_park_api.web.exception;

public class CodigUniqueViolationException extends RuntimeException {
    public CodigUniqueViolationException(String message) {
        super(message);
    }
}
