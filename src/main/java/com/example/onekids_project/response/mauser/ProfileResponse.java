package com.example.onekids_project.response.mauser;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileResponse extends IdResponse {
    private String username;

    private String fullName;

    private String gender;

    private LocalDate birthday;

    private String phone;

    private String avatar;

    private String email;

    private String appType;

}
