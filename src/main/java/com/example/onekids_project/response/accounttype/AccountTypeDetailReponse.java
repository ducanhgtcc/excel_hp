package com.example.onekids_project.response.accounttype;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTypeDetailReponse extends IdResponse {
    private String name;

    private String description;
}
