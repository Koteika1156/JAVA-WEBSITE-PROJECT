package com.example.demo.exception;

public class DifferentDatesException extends RuntimeException {
  public DifferentDatesException() {
    super();
  }

  public DifferentDatesException(String message) {
    super(message);
  }
}
