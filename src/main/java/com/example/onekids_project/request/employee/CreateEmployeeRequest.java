package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.department.TabDepartmentRequest;
import com.example.onekids_project.request.department.TabProfessionalRequest;
import com.example.onekids_project.request.kids.AppIconNotifyPlusRequest;
import com.example.onekids_project.request.kids.AppIconPlusRequest;
import com.example.onekids_project.request.kids.AppIconTeacherRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateEmployeeRequest {
    private CreateEmployeeMainInfoRequest createEmployeeMainInfoRequest;
    private List<TabDepartmentRequest> tabDepartmentRequestList;
    private List<TabProfessionalRequest> tabProfessionalRequestList;
    private List<AppIconTeacherRequest> appIconTeacherRequestList;
    private List<AppIconPlusRequest> appIconPlusRequestList;
    private List<AppIconNotifyPlusRequest> notifyAppIconPlusRequestList;
}
