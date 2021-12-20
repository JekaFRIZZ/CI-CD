package com.epam.esm.exception;

public class DuplicateResourceException extends CustomException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, int errorCode) {
        super(message, errorCode);
    }

    public DuplicateResourceException() {
    }
}
