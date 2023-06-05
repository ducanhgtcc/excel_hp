package com.example.onekids_project.request.mauser;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UsernameRequest {
    @NotBlank
    @Size(min = 6)
    private String username;

    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;
}
