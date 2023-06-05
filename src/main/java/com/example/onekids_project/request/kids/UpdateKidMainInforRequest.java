package com.example.onekids_project.request.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UpdateKidMainInforRequest extends IdRequest {
    @NotBlank
    @StringInList(values = {KidsStatusConstant.STUDYING, KidsStatusConstant.STUDY_WAIT, KidsStatusConstant.RESERVE, KidsStatusConstant.LEAVE_SCHOOL})
    private String kidStatus;

    @NotBlank
    private String fullName;

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

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private String note;

    @NotBlank
    @StringInList(values = {AppConstant.FATHER, AppConstant.MOTHER})
    private String representation;

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
