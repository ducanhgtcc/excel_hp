package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileMobileResponse {
    private String username;

    private String password;

    private String fullName;

    private String gender;

    private String birthday;

    private String phone;

    private String avatar;

    private String email;
}
