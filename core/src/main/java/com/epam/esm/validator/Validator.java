package com.epam.esm.validator;

public interface Validator<T> {
    static final int MIN_LENGTH_NAME = 2;
    static final int MAX_LENGTH_NAME = 30;

    void validate(T entity);
}
