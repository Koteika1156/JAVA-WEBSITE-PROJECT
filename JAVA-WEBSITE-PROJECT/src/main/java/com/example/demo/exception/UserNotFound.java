package com.example.demo.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super();
    }

    public UserNotFound(String message) {
        super(message);
    }
}
