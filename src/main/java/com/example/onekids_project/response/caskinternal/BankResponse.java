package com.example.onekids_project.response.caskinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankResponse extends IdResponse {

    private String fullName;

    private String accountNumber;

    private String bankName;

    private String branch;

    private String description;

    private boolean checked;
}
