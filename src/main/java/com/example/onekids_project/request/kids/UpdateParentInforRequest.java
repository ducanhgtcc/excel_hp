package com.example.onekids_project.request.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateParentInforRequest {

    private Long id;

    @NotBlank
    @StringInList(values = {AppConstant.FATHER, AppConstant.MOTHER})
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
