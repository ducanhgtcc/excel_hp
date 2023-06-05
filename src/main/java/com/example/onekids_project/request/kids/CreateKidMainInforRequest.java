package com.example.onekids_project.request.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CreateKidMainInforRequest {
    @NotNull
    private Long idGrade;

    @NotNull
    private Long idClass;

    @NotBlank
    @StringInList(values = {KidsStatusConstant.STUDYING, KidsStatusConstant.STUDY_WAIT})
    private String kidStatus;

    @NotBlank
    private String fullName;

    @NotBlank
    @StringInList(values = {AppConstant.FATHER, AppConstant.MOTHER})
    private String representation;

    @NotNull
    private LocalDate birthDay;

    @NotBlank
    @StringInList(values = {AppConstant.MALE, AppConstant.FEMALE})
    private String gender;

    private String nickName;

    private String address;

    private String permanentAddress;

    private String ethnic;

    @NotNull
    private LocalDate dateStart;

    private String note;


    //infor parents
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
