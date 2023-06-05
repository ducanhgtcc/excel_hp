package com.example.onekids_project.mobile.teacher.response.phonebook;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KidTeacherResponse {
    private String fullName;

    private String gender;

    private String birthDay;

    private String parentPhone = "";

    private String avatarKid;

    private String address;

    private String fatherName;

    private String motherName;

    private String fatherPhone = "";

    private String motherPhone = "";

    private String fatherEmail;

    private String motherEmail;
}
