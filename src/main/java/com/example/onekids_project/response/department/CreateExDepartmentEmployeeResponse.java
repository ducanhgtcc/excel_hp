package com.example.onekids_project.response.department;

import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateExDepartmentEmployeeResponse{

    List<ExDepartmentEmployeeDTO> exDepartmentEmployeeDTOList;
    /*private String departmentName;

    private String departmentDescription;

    private Long idSchool;*/
}
