package com.example.onekids_project.mobile.parent.request.finance;

import com.example.onekids_project.common.FinanceMobileConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * date 2021-03-16 13:07
 *
 * @author lavanviet
 */
@Data
public class OrderKidsParentRequest {
    @NotNull
    private Integer year;

    @StringInList(values = {FinanceMobileConstant.PAID_FULL, FinanceMobileConstant.PAID_PART, FinanceMobileConstant.PAID_EMPTY})
    private String paidStatus;
}
