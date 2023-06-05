package com.example.onekids_project.request.employee;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateEmployeeMainInfoRequest extends IdRequest {

//    private List<String> accountType;

    private List<Long> idAccountTypeList;

    @NotBlank
    private String phone;

    private String firstName;

    private String lastName;

    @NotBlank
    private String fullName;

    @NotNull
    private LocalDate birthday;

    @NotBlank
    @StringInList(values = {EmployeeConstant.STATUS_WORKING, EmployeeConstant.STATUS_RETAIN, EmployeeConstant.STATUS_LEAVE})
    private String employeeStatus;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    @StringInList(values = {AppConstant.MALE, AppConstant.FEMALE})
    private String gender;

    private String address;

    private String email;

    private String cmnd;

    private LocalDate cmndDate;

    private String permanentAddress;

    private String currentAddress;

    private String marriedStatus;

    private Integer numberChildren;

    private String educationLevel;

    private String professional;

    private LocalDate contractDate;

    private LocalDate endDate;

    private String note;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private String ethnic;

//    private boolean activated;
//
//    private Boolean smsSend;
//
//    private Boolean smsReceive;

//    private List<String> listAppType;

//    private Long idEmployee;

//    private Long idSchool;

}
