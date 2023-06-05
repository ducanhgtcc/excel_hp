package com.example.onekids_project.enums;

public enum EmployeeStatusEnum {

    EMPLOYEE_STATUS_WORKING("Đang làm"),
    EMPLOYEE_STATUS_RETAIN("Tạm nghỉ"),
    EMPLOYEE_STATUS_LEAVE("Nghỉ làm");

    private String value;

    EmployeeStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
