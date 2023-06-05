package com.example.onekids_project.response.phone;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountLoginResponse extends IdResponse {

    private String phone;

    private String username;

    private String passwordShow;


}
