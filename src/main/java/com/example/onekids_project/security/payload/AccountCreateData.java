package com.example.onekids_project.security.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateData {
    private String fullName;

    private String username;

    private String password;

    private String appType;

    private String phone;

    private String gender;
}
