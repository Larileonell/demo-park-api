package com.allanaoliveira.demo_park_api.exception;

public class cpfUniqueViolationException extends RuntimeException {
    public cpfUniqueViolationException(String message) {
        super(message);
    }
}
