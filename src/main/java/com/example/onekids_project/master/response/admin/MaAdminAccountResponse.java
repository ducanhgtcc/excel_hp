package com.example.onekids_project.master.response.admin;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaAdminAccountResponse extends IdResponse {
    private String fullName;

    private String phone;

    private String username;

    private String password;

    private boolean isActivated;

}
