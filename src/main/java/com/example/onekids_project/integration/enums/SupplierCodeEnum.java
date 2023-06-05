package com.example.onekids_project.integration.enums;

public enum SupplierCodeEnum {

    NEO("NEO"),
    VNPT("VNPT");

    private String value;

    SupplierCodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    }
