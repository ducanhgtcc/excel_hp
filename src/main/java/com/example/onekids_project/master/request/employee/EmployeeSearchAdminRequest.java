package com.example.onekids_project.master.request.employee;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EmployeeSearchAdminRequest extends PageNumberWebRequest {
    @NotNull
    private Long idAgent;

    private Long idSchool;

    private boolean deleteStatus;

    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER})
    private String appType;

    @NotBlank
    @StringInList(values = {EmployeeConstant.STATUS_WORKING, EmployeeConstant.STATUS_RETAIN, EmployeeConstant.STATUS_LEAVE})
    private String status;

    private String nameOrPhone;

    @Override
    public String toString() {
        return "EmployeeSearchAdminRequest{" +
                "idAgent=" + idAgent +
                ", idSchool=" + idSchool +
                ", deleteStatus=" + deleteStatus +
                ", appType='" + appType + '\'' +
                ", status='" + status + '\'' +
                ", nameOrPhone='" + nameOrPhone + '\'' +
                "} " + super.toString();
    }
}
