package com.example.onekids_project.request.accounttype;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountTypeCreateRequest {
    @NotBlank
    private String name;

    private String description;
}
