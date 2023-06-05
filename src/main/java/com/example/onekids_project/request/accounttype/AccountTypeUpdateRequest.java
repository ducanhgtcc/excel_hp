package com.example.onekids_project.request.accounttype;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountTypeUpdateRequest extends IdResponse {
    @NotBlank
    private String name;

    private String description;
}
