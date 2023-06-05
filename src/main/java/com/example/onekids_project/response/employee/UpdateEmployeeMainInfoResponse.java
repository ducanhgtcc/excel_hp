package com.example.onekids_project.response.employee;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateEmployeeMainInfoResponse extends IdResponse {
    private List<String> accountType;

    private String phone;

    private String firstName;

    private String lastName;

    private String fullName;

    private String avatar;

    private LocalDate birthday;

    private String address;

    private String gender;

    private String email;

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

    private boolean historyView = AppConstant.APP_TRUE;

    private Boolean isActivatedTeacher;

    private Boolean isActivatedPlus;

    private Boolean smsSend;

    private Boolean smsReceive;

    private String listAppType;

    private Long idEmployee;

    private Long idSchool;
}

