package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class KidsPackageInClassSearchRequest extends DateNotNullRequest {

//    @NotBlank
//    @StringInList(values = {FinanceConstant.PAST_MONTH, FinanceConstant.NOW_MONTH, FinanceConstant.NEXT_MONTH})
//    private String month;

    @NotBlank
    @StringInList(values = {KidsStatusConstant.STUDYING, KidsStatusConstant.STUDY_WAIT, KidsStatusConstant.RESERVE, KidsStatusConstant.LEAVE_SCHOOL})
    private String status;

    @NotNull
    private Long idClass;

    private String fullName;

}
