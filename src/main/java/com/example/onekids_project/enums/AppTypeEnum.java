package com.example.onekids_project.enums;

public enum AppTypeEnum {

    PLUS("plus"),
    TEACHER("teacher"),
    PARENT("parent");

    private String value;

    AppTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
