package com.example.onekids_project.master.request.school;

import com.example.onekids_project.common.AccountTypeConstant;
import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SearchAccountRequest extends PageNumberWebRequest {
    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String appType;

    @NotBlank
    @StringInList(values = {AppConstant.DELETE_TRUE, AppConstant.DELETE_FALSE, AppConstant.DELETE_COMPLETE})
    private String deleteStatus;

    private String nameOrPhone;

    private Boolean active;

    @StringInList(values = {AccountTypeConstant.DELETE_MANUAL, AccountTypeConstant.DELETE_HANDLE, AccountTypeConstant.DELETE_AUTO})
    private String typeDelete;

    @StringInList(values = {AccountTypeConstant.ACCOUNT_YES, AccountTypeConstant.ACCOUNT_NO})
    private String typeChildren;

    @Override
    public String toString() {
        return "SearchAccountRequest{" +
                "appType='" + appType + '\'' +
                ", deleteStatus=" + deleteStatus +
                ", nameOrPhone='" + nameOrPhone + '\'' +
                "} " + super.toString();
    }
}
