package com.example.onekids_project.request.finance.statistical;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * date 2021-03-19 09:33
 *
 * @author lavanviet
 */
@Data
public class FinanceKidsStatisticalRequest {
    @NotNull
    private Integer year;

    @NotNull
    private Integer startMonth;

    @NotNull
    private Integer endMonth;
}
