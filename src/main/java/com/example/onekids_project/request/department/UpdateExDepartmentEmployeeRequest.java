package com.example.onekids_project.request.department;

import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExDepartmentEmployeeRequest extends IdRequest {

    private Department department;

    private Long idDepartment;

    private Employee employee;

    private Long idEmployee;

    private String position;
}
