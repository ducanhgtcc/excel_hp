package com.example.onekids_project.response.finance.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-11 13:35
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class ListOrderKidsResponse {
    private long moneyTotalIn;

    private long moneyPaidIn;

    private long moneyTotalOut;

    private long moneyPaidOut;

    private List<OrderKidsCustom1> dataList;
}
