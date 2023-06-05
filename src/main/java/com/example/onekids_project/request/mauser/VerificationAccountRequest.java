package com.example.onekids_project.request.mauser;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class VerificationAccountRequest extends IdRequest {
    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;

    //mã xác thực: so sánh với 2 mã trong DB, đúng 1 trong 2 là ok
    @NotBlank
    private String code;

    @Override
    public String toString() {
        return "VerificationAccountRequest{" +
                "appType='" + appType + '\'' +
                ", code='" + code + '\'' +
                "} " + super.toString();
    }
}
