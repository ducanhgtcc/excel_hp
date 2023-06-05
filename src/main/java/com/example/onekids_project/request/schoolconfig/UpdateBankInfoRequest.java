package com.example.onekids_project.request.schoolconfig;

import lombok.Data;

@Data
public class UpdateBankInfoRequest {

    private String bankInfo;

    private String note;

    private String expired;

    private String feesParent;
}
