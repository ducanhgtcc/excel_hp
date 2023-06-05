package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateExEmployeeClassRequest extends IdRequest {

    private String accountType;

    private String firstName;

    private String lastName;

    private String fullName;

    private String avatar;

    private LocalDate birthday;

    private String address;

    private String gender;

    private String email;

    private String phone;

    private String cmnd;

    private LocalDate cmndDate;

    private String permanentAddress;

    private String currentAddress;

    private String marriedStatus;

    private Integer numberChildren;

    private String educationLevel;

    private String professional;

    private LocalDate startDate;

    private LocalDate contractDate;

    private LocalDate endDate;

    private String note;

    private String employeeStatus;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private String employeeType;

    private String appType;

    private int schoolCount;

    private String listIdClass;

    private Long idDepartmentEmployee;
}
