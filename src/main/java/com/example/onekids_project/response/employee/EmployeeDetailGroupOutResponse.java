package com.example.onekids_project.response.employee;

import com.example.onekids_project.common.AppConstant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeDetailGroupOutResponse {

    private String[] accountType;

    //    private List<String> accountTypeList;
    private List<Long> idAccountTypeList;

    private String code;

    private String phone;

    private String firstName;

    private String lastName;

    private String fullName;

    private String avatarView;

    private LocalDate birthday;

    private String address;

    private String gender;

    private String email;

    private String cmnd;

    private LocalDate cmndDate;

    private String departmentName;

    private String className;

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

    private boolean activated;

    private Boolean smsSend;

    private Boolean smsReceive;

    private boolean phoneDuplicate;

    private String appType;

    private Long idEmployee;

    private Long idSchool;

    private String passwordUser;
}
