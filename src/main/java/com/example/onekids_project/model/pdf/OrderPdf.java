package com.example.onekids_project.model.pdf;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-08 14:39
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class OrderPdf {

    private List<KidsPackagePdf> dataList;

    //số dư ví
    private double moneyWallet;

    //tổng tiền phải thanh toán
    private double moneyTotal;

    //tổng tiền đã trả
    private double moneyPaidTotal;

    //tổng tiền còn thiếu
    private double moneyRemain;

}
