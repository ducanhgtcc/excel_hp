package com.example.onekids_project.request.mauser;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class HandleNewPhoneRequest extends IdRequest {
    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;

    @NotBlank
    private String newPhone;

    @Override
    public String toString() {
        return "HandleNewPhoneRequest{" +
                "appType='" + appType + '\'' +
                ", newPhone='" + newPhone + '\'' +
                "} " + super.toString();
    }
}
