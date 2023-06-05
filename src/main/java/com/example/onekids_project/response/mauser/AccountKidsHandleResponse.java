package com.example.onekids_project.response.mauser;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountKidsHandleResponse extends IdResponse {
    private String fullName;

    private String phone;

    private String representation;

    private String username;

    private LocalDate birthDay;

    private String kidStatus;

    private LocalDateTime createdDate;

    private boolean eixstAccount;

    private boolean status;
}
