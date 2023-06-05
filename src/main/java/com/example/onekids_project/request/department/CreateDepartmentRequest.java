package com.example.onekids_project.request.department;

import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.school.School;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateDepartmentRequest {

    private String departmentName;

    private String departmentDescription;

    private List<ExDepartmentEmployeeDTO> departmentEmployeeList;
}
