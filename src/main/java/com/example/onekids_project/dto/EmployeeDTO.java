package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDTO extends BaseDTO<String> {

    private String employeeCode;

    private String[] accountType;

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

    private boolean historyView;

    private Boolean isActivated;

    private Long idSchool;

    private String appType;

    private int schoolCount;

    private String listIdClass;

    int pageNumber=0;

    int maxPageItem=5;

    String checked="";

    private MaUserDTO maUser;
    String listDepartment;

/*
    private List<EmployeeHistory> employeeHistoryList;

    private List<AbsentTeacher> asentTeacherList;

    private List<ExEmployeeClass> exEmployeeClassList;

    private List<AttendanceTeacher> attendanceTeacherList;*/
}
