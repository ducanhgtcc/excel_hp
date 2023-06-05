package com.example.onekids_project.response.birthdaymanagement;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ParentResponse extends IdResponse {
    private String parentCode;

    private String accountType;

    private String fatherAvatar;

    private String representation;

    private String fatherName;

    private LocalDate fatherBirthday;

    private String fatherPhone;

    private String fatherEmail;

    private String fatherGender;

    private String fatherJob;

    private String motherAvatar;

    private String motherName;

    private LocalDate motherBirthday;

    private String motherPhone;

    private String motherEmail;

    private String motherGender;

    private String motherJob;

    private Long idKidLogin;
}
