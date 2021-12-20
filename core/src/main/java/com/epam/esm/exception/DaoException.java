package com.epam.esm.exception;

public class DaoException extends CustomException{

    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, int errorCode) {
        super(message, errorCode);
    }
}
