package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SupperAdminRequest {
    private String fullName;

    private String phone;

    private String email;

    private LocalDate birthDay;

    private String gender;

    private String note;
}
