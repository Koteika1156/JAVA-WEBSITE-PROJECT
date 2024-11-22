package com.example.demo.exception;

public class OutsideClinicHoursException extends RuntimeException {
    public OutsideClinicHoursException() {
        super();
    }

    public OutsideClinicHoursException(String message) {
        super(message);
    }
}
