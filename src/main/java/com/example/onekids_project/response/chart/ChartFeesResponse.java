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
public class ChartFeesResponse {

    private String name;
    //Theo hóa đơn
    private long feesYes;

    private long feesNo;

    private long feesUn;
    //Theo tiền
    private long moneyOut;

    private long moneyIn;
}
