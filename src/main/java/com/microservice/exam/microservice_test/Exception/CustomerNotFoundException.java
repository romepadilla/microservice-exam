package com.microservice.exam.microservice_test.Exception;

public class CustomerNotFoundException extends RuntimeException {

    // Constructor to accept a custom error message
    public CustomerNotFoundException(String message) {
        super(message);
    }

    // Optionally, you can include another constructor that accepts a message and a cause
    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

