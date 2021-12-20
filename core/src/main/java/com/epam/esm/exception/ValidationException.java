package com.epam.esm.exception;

public class ValidationException extends CustomException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, int errorCode) {
        super(message, errorCode);
    }

    public ValidationException() {
    }
}
