package com.example.onekids_project.request.department;

import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.school.Department;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateExDepartmentEmployeeRequest {
   List<ExDepartmentEmployeeDTO> exDepartmentEmployeeDTOList;
}
