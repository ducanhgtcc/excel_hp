package com.example.onekids_project.master.response.employee;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.employee.DepartmentEmployeeOtherResponse;
import com.example.onekids_project.response.employee.EmployeeClassOtherResponse;
import com.example.onekids_project.response.school.SchoolOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EmployeeAdminResponse extends IdResponse {
    private String fullName;

    private String phone;

    private LocalDate birthday;

    private String phoneSMS;

    private String username;

    private String password;

    private String email;

    private List<DepartmentEmployeeOtherResponse> departmentEmployeeList;

    private List<EmployeeClassOtherResponse> exEmployeeClassList;

    private boolean activated;

    private boolean smsReceive;

    private boolean login;

    private SchoolOtherResponse school;

    private LocalDateTime createdDate;

}
