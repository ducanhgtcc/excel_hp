package com.example.onekids_project.request.finance.statistical;

import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * date 2021-03-19 09:33
 *
 * @author lavanviet
 */
@Data
public class FinanceKidsStatisticalMiniRequest {
    @NotNull
    private Integer year;

    @NotNull
    private Integer startMonth;

    @NotNull
    private Integer endMonth;

    private Long idClass;

    @StringInList(values = {KidsStatusConstant.STUDYING, KidsStatusConstant.STUDY_WAIT, KidsStatusConstant.RESERVE, KidsStatusConstant.LEAVE_SCHOOL})
    private String status;
}
