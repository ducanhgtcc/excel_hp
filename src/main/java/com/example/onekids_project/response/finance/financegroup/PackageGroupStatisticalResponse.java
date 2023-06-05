package com.example.onekids_project.response.finance.financegroup;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-02 10:04
 *
 * @author lavanviet
 */
@Getter
@Setter
public class PackageGroupStatisticalResponse extends IdResponse {
    private String name;

    private String note;

    private double moneyTotalInOut;

    private double moneyTotalInOutPaid;

    private double moneyTotalInOutRemain;
}
