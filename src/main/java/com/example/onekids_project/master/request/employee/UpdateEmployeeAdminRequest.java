package com.example.onekids_project.master.request.employee;

import com.example.onekids_project.request.department.TabDepartmentRequest;
import com.example.onekids_project.request.department.TabProfessionalRequest;
import com.example.onekids_project.request.employee.UpdateEmployeeMainInfoRequest;
import com.example.onekids_project.request.kids.AppIconNotifyPlusRequest;
import com.example.onekids_project.request.kids.AppIconPlusRequest;
import com.example.onekids_project.request.kids.AppIconTeacherRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Data
public class UpdateEmployeeAdminRequest {
    @Valid
    private UpdateEmployeeMainInfoRequest updateEmployeeMainInfoRequest;
}
