package com.example.onekids_project.request.cashinternal;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateBankInforRequest {

    @NotNull
    private String fullName;

    @NotNull
    private String accountNumber;

    @NotNull
    private String bankName;

    private String branch;

    private String description;

}
