package com.example.onekids_project.response.finance.exportimport;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-20 13:19
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsExport {
    private String code;

    private double moneyTotal;

    private double moneyPaidTotal;

    private double moneyRemainInTotal;

    private double moneyRemainOutTotal;
}
