package com.epam.esm.audit;

public enum AuditType {
    
    UPDATE("UPDATE"), INSERT("INSERT"), REMOVE("REMOVE");

    private final String value;

    AuditType(String value) {
        this.value = value;
    }
}
