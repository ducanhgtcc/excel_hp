package com.example.onekids_project.request.mauser;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountInforRequest {

    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;

    @NotBlank
    private String phone;
}
