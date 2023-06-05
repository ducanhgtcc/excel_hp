package com.example.onekids_project.enums;

public enum StudentStatusEnum {

    STUDYING("Đang học"),
    STUDY_WAIT("Chờ học"),
    RESERVE("Bảo lưu"),
    LEAVE_SCHOOL("Nghỉ học");

    private String value;

    StudentStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
