package com.example.demo.exception;

public class RecordNotFound extends RuntimeException {
    public RecordNotFound() {
        super();
    }

    public RecordNotFound(String message) {
        super(message);
    }
}
