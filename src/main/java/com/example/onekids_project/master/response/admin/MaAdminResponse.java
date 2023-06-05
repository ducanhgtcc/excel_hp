package com.example.onekids_project.master.response.admin;

import com.example.onekids_project.master.response.MaUserOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class MaAdminResponse extends IdResponse {

    private String code;

    private String accountType;

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

    private LocalDateTime endDate;

    private String note;

    private String adminStatus;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private MaUserOtherResponse maUser;

    private LocalDateTime createdDate;


}
