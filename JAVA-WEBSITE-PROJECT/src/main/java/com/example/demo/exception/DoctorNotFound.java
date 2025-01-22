package com.example.demo.exception;

public class DoctorNotFound extends RuntimeException{
    public DoctorNotFound() {
        super();
    }

    public DoctorNotFound(String message) {
        super(message);
    }
}
