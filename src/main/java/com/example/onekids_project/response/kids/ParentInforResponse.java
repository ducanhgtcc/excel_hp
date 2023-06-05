package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ParentInforResponse extends IdResponse {

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
