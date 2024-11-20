package com.example.demo.exception;

public class RecordAlreadyExist extends RuntimeException {
    public RecordAlreadyExist() {
        super();
    }

    public RecordAlreadyExist(String message) {
        super(message);
    }
}
