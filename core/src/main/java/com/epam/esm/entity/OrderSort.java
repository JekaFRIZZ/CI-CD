package com.epam.esm.entity;

public enum OrderSort {
    ASC(true), DESC(false);
    private final boolean value;

    OrderSort(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }
}
