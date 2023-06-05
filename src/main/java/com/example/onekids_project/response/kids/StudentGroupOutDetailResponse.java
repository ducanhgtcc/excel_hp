package com.example.onekids_project.response.kids;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-07-13 2:24 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class StudentGroupOutDetailResponse {

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
}
