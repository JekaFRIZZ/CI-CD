package com.epam.esm.service;

public interface Service<T> {

    default void validatePaginationParams(int limit, int offset) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException("Pagination parameters are negative");
        }
    }
}
