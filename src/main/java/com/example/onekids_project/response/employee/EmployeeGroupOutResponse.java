package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeGroupOutResponse extends IdResponse {
    private String code;

    private String fullName;

    private LocalDate birthday;

    private String gender;

    private String phone;

    private LocalDate startDate;

    private LocalDate outDate;

    private List<DepartmentEmployeeOtherResponse> departmentEmployeeList;

    private List<EmployeeClassOtherResponse> exEmployeeClassList;

}
