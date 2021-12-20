package com.epam.esm.exception;

public class ResourceExistenceException extends CustomException {
    public ResourceExistenceException(String message) {
        super(message);
    }

    public ResourceExistenceException(String message, int errorCode) {
        super(message, errorCode);
    }

    public ResourceExistenceException() {
    }
}
