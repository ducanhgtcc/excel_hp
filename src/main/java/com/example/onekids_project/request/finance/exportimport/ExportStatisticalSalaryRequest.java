package com.example.onekids_project.request.finance.exportimport;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * date 2021-03-19 09:33
 *
 * @author lavanviet
 */
@Data
public class ExportStatisticalSalaryRequest {
    @NotBlank
    @StringInList(values = {FinanceConstant.EXPORT_IN, FinanceConstant.EXPORT_OUT, FinanceConstant.EXPORT_INOUT, FinanceConstant.EXPORT_ORDER, FinanceConstant.EXPORT_INOUT_TRUE, FinanceConstant.EXPORT_INOUT_TOTAL})
    private String type;

    @NotNull
    private Integer startMonth;

    @NotNull
    private Integer endMonth;

    @NotNull
    private Integer year;

    @NotNull
    private String status;
}
