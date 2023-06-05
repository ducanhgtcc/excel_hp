package com.example.onekids_project.mobile.request;

import com.example.onekids_project.common.DeviceTypeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class DeviceLoginMobileRequest {

    @NotBlank
    private String idDevice;

    @NotBlank
    @StringInList(values = {DeviceTypeConstant.ANDROID, DeviceTypeConstant.IOS})
    private String type;

    @NotBlank
    private String tokenFirebase;
}
