package com.example.onekids_project.request.commononekids;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePhoneSMSRequest extends IdRequest {
    @NotBlank
    @StringInList(values = {AppTypeConstant.PARENT, AppTypeConstant.TEACHER, AppTypeConstant.SCHOOL})
    private String appType;

    @NotBlank
    private String newPhone;

    @Override
    public String toString() {
        return "ChangePhoneSMSRequest{" +
                "appType='" + appType + '\'' +
                ", newPhone='" + newPhone + '\'' +
                "} " + super.toString();
    }
}
