package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.department.TabDepartmentRequest;
import com.example.onekids_project.request.department.TabProfessionalRequest;
import com.example.onekids_project.request.kids.AppIconNotifyPlusRequest;
import com.example.onekids_project.request.kids.AppIconPlusRequest;
import com.example.onekids_project.request.kids.AppIconTeacherRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Data
public class UpdateEmployeeRequest{
    private Long idSchool;
    @Valid
    private UpdateEmployeeMainInfoRequest updateEmployeeMainInfoRequest;
    private List<TabDepartmentRequest> tabDepartmentRequestList;
    private List<TabProfessionalRequest> tabProfessionalRequestList;
    private List<AppIconTeacherRequest> appIconTeacherRequestList;
    private List<AppIconPlusRequest> appIconPlusRequestList;
    private List<AppIconNotifyPlusRequest> notifyAppIconPlusRequestList;
}
