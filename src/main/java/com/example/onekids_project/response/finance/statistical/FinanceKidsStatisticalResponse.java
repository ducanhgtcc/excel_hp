package com.example.onekids_project.response.finance.statistical;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-19 09:35
 *
 * @author lavanviet
 */
@Getter
@Setter
public class FinanceKidsStatisticalResponse {
    //tổng thu
    private long moneyTotalIn;

    //tổng đã thu
    private long moneyTotalPaidIn;

    //tổng còn lại thu(tổng thu-tổng đã thu)
    private long moneyTotalRemainIn;

    //tổng chi
    private long moneyTotalOut;

    //tổng đã chi
    private long moneyTotalPaidOut;

    //tổng còn lại chi(tổng chi-tổng đã chi)
    private long moneyTotalRemainOut;

    //tổng (tổng thu-tổng chi): áp dụng cho khoản học sinh
    private long moneyTotalInOut;

    //tổng (tổng đã thu-tổng đã chi) (đã chi-đã thu)
    private long moneyTotalInOutPaid;

    //tổng (tổng còn lại thu-tổng còn lại chi) hoặc (còn lại chi-còn lại thu)
    private long moneyTotalInOutRemain;

    private int kidsNumber;

    private int orderNumber;
}
