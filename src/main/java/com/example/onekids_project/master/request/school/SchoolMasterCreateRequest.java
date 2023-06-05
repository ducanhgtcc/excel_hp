package com.example.onekids_project.master.request.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class SchoolMasterCreateRequest {

    @NotBlank
    @Size(min = 6)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    private Long idSchool;

    @NotBlank
    private String fullName;

    private LocalDate birthDay;

    @NotBlank
    private String phone;

    private String email;

    @NotBlank
    @StringInList(values = {AppConstant.MALE, AppConstant.FEMALE})
    private String gender;

    private String note;
}
