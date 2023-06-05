package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ma_employee_temp")
public class EmployeeTemp extends BaseEntity<String> {

    @Column(length = 45, nullable = false)
    private String employeeCode;

    @Column(nullable = false)
    private String accounType;

    @Column(length = 45)
    private String firstName;

    @Column(length = 45)
    private String lastName;

    @Column(length = 45, nullable = false)
    private String fullName;

    private String avatar;

    private LocalDate birthDay;

    @Column(length = 1000)
    private String address;

    @Column(length = 45)
    private String gender;

    private String email;

    private String phone;

    @Column(length = 20)
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

    private LocalDateTime endDate;

    private String note;

    private String employeeStatus;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private Boolean historyView;

    private boolean isActivated;

    @Column(nullable = false)
    private String employeeType;

    private Long idClass;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idDepartment;

    private String appType;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int schoolCount;

    @Column(nullable = false, length = 6)
    private String verificationCodesTeacher;

    @Column(nullable = false, length = 6)
    private String verificationCodesPlus;

}
