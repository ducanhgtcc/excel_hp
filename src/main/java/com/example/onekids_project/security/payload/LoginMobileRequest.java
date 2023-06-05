package com.example.onekids_project.security.payload;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.DeviceTypeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginMobileRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;

    private boolean camera;

    private String idDevice;

    //android, ios
    @StringInList(values = {DeviceTypeConstant.ANDROID, DeviceTypeConstant.IOS})
    private String deviceType;
}
