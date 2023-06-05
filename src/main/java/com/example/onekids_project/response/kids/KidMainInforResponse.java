package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KidMainInforResponse extends IdResponse {
    private Long idGrade;

    private String gradeName;

    private Long idClass;

    private String className;

    private String kidStatus;

    private String avatarKid;

    private String kidCode;

    private String fullName;

    private LocalDate birthDay;

    private String gender;

    private String nickName;

    private String address;

    private String permanentAddress;

    private String ethnic;

    private LocalDate dateStart;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private String note;


    private String representation;

    private String fatherName;

    private LocalDate fatherBirthday;

    private String fatherPhone;

    private String fatherEmail;

    private String fatherJob;

    private String motherName;

    private LocalDate motherBirthday;

    private String motherPhone;

    private String motherEmail;

    private String motherJob;

    private String identificationNumber;
}
