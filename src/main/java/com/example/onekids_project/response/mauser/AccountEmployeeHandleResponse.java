package com.example.onekids_project.response.mauser;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountEmployeeHandleResponse extends IdResponse {
    private String fullName;

    private String phone;

    private String username;

    private LocalDate birthday;

    private String employeeStatus;

    private LocalDateTime createdDate;

    private boolean eixstAccount;

    private boolean status;
}
