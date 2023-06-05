package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeePlusNewResponse extends IdResponse {
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

    private boolean smsSend;

    private boolean login;


}
