package com.example.demo.exception;

public class CrossingTimeException extends RuntimeException {
    public CrossingTimeException() {
        super();
    }

    public CrossingTimeException(String message) {
        super(message);
    }
}
