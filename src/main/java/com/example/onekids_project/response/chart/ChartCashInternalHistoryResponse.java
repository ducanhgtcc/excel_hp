package com.example.onekids_project.response.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-09-21 8:56 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ChartCashInternalHistoryResponse {

    private String name;

    private double total;

    private double moneySchool;

    private double moneyKids;

    private double moneyEmployee;
}
