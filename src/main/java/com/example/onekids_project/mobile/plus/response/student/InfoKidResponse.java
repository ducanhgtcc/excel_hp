package com.example.onekids_project.mobile.plus.response.student;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InfoKidResponse {
    private String avatarKid;

    private String fullName;

    private String gender;

    private String birthDay;

    private String phoneParent;

    private String kidStatus;

    private String fatherName;

    private String fatherPhone;

    private String fatherEmail;

    private String motherName;

    private String motherPhone;

    private String motherEmail;

    private String address;
}
