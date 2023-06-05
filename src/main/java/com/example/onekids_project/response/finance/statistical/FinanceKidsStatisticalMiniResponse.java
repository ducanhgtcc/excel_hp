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
public class FinanceKidsStatisticalMiniResponse {
    //tổng (tổng thu-tổng chi): áp dụng cho khoản học sinh
    private long moneyTotalInOut;

    //tổng (tổng đã thu-tổng đã chi) (đã chi-đã thu)
    private long moneyTotalInOutPaid;

    //tổng (tổng còn lại thu-tổng còn lại chi) hoặc (còn lại chi-còn lại thu)
    private long moneyTotalInOutRemain;
}
