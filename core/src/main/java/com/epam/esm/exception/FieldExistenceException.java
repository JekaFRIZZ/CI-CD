package com.epam.esm.exception;

public class FieldExistenceException extends CustomException {
    public FieldExistenceException(String message) {
        super(message);
    }

    public FieldExistenceException(String message, int errorCode) {
        super(message, errorCode);
    }

    public FieldExistenceException() {
    }
}