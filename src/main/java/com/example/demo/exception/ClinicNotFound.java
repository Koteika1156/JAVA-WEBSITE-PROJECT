package com.example.demo.exception;

public class ClinicNotFound extends RuntimeException {
    public ClinicNotFound() {
        super();
    }

    public ClinicNotFound(String message) {
        super(message);
    }
}
