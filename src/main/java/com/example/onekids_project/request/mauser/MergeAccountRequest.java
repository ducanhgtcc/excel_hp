package com.example.onekids_project.request.mauser;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MergeAccountRequest extends IdRequest {
    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;

    //oldAccount: dùng tài khoản cũ
    //newAccount: dùng tài khoản mới
    @NotBlank
    @StringInList(values = {AppConstant.OLD_ACCOUNT, AppConstant.NEW_ACCOUNT})
    private String type;

    @NotBlank
    private String phone;

    private List<AccountAndStatusRequest> userList;

    @Override
    public String toString() {
        return "MergeAccountRequest{" +
                "appType='" + appType + '\'' +
                ", type='" + type + '\'' +
                ", phone='" + phone + '\'' +
                ", userList=" + userList +
                "} " + super.toString();
    }
}
