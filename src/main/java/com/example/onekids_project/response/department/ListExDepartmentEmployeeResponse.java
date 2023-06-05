package com.example.onekids_project.response.department;

import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListExDepartmentEmployeeResponse {
    List<ExDepartmentEmployeeDTO> exDepartmentEmployeeList;
}
