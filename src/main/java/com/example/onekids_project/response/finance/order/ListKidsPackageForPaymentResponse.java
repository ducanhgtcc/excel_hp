package com.example.onekids_project.response.finance.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-02-26 11:22
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ListKidsPackageForPaymentResponse {

    //số tiền có trong ví
    private double moneyWallet;

    //tổng số tiền của hóa đơn
    private double moneyTotal;

    //tổng số tiền đã trả
    private double moneyTotalPaid;

    //thời gian chỉnh sửa gần nhất của orderKids
    private LocalDateTime dateTime;

    private String description;

    private List<KidsPackageForPaymentResponse> dataList;

    private String transferMoneyType;
}
