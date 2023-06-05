package com.example.onekids_project.model.finance;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-05-20 10:02
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderMoneyModel {
    //tổng thu
    private double moneyTotalIn;

    //tổng đã thu
    private double moneyTotalPaidIn;

    //tổng còn lại thu(tổng thu-tổng đã thu)
    private double moneyTotalRemainIn;

    //tổng chi
    private double moneyTotalOut;

    //tổng đã chi
    private double moneyTotalPaidOut;

    //tổng còn lại chi(tổng chi-tổng đã chi)
    private double moneyTotalRemainOut;

    //tổng (tổng thu-tổng chi): áp dụng cho khoản học sinh
    // tổng (tổng chi-tổng thu): áp dụng cho khoản nhân sự
    private double moneyTotalInOut;

    //tổng (tổng đã thu-tổng đã chi) (đã chi-đã thu)
    private double moneyTotalInOutPaid;

    //tổng (tổng còn lại thu-tổng còn lại chi) hoặc (còn lại chi-còn lại thu)
    private double moneyTotalInOutRemain;
}
