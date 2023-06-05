package com.example.onekids_project.request.cyclemoney;

import com.example.onekids_project.common.CycleMoneyConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author lavanviet
 */
@Data
public class CycleMoneyRequest {
    @NotBlank
    @StringInList(values = {CycleMoneyConstant.TYPE_DEFAULT, CycleMoneyConstant.TYPE_CUSTOM})
    private String typeFees;

    @Min(1)
    private int startDateFees;

    @Min(1)
    private int endDateFees;

    //range1, range2
    @NotBlank
    @StringInList(values = {CycleMoneyConstant.RANGE1, CycleMoneyConstant.RANGE2})
    private String rangeFees;


    @NotBlank
    @StringInList(values = {CycleMoneyConstant.TYPE_DEFAULT, CycleMoneyConstant.TYPE_CUSTOM})
    private String typeSalary;

    @Min(1)
    private int startDateSalary;

    @Min(1)
    private int endDateSalary;

    @NotBlank
    @StringInList(values = {CycleMoneyConstant.RANGE1, CycleMoneyConstant.RANGE2})
    private String rangeSalary;

    @NotBlank
    @StringInList(values = {CycleMoneyConstant.TRANSFER_MONEY_WALLET, CycleMoneyConstant.TRANSFER_MONEY_MONTH})
    private String transferMoneyType;
}
