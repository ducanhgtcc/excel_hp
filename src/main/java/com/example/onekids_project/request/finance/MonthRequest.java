package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MonthRequest {
    @NotBlank
    @StringInList(values = {FinanceConstant.PAST_MONTH, FinanceConstant.NOW_MONTH, FinanceConstant.NEXT_MONTH})
    private String monthInput;
}
