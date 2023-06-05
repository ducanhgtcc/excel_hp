package com.example.onekids_project.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-03-03 08:27
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsHistoryResponse extends IdResponse {
    private String category;

    private String name;

    private LocalDate date;

    private double moneyInput;

    private double moneyWallet;

    private String description;

    //tổng số tiền đã thanh toán cho các khoản thu
    private double moneyPayment;
}
