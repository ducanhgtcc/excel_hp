package com.example.onekids_project.request.smsNotify;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class VerifyCodeRequest extends IdRequest {

    @NotBlank
    private String phone;

    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;

    @Override
    public String toString() {
        return "VerifyCodeRequest{" +
                "phone='" + phone + '\'' +
                ", appType='" + appType + '\'' +
                '}';
    }
}
