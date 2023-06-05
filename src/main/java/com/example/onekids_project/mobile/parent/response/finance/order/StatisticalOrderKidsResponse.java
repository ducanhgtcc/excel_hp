package com.example.onekids_project.mobile.parent.response.finance.order;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-18 08:42
 *
 * @author lavanviet
 */
@Getter
@Setter
public class StatisticalOrderKidsResponse {
    //số lượng hóa đơn chưa hoàn thành
    private int orderNumber;

    //số lần nhà trường rút chưa xác nhận
    private int walletNumber;
}
