package com.example.onekids_project.mobile.parent.response.finance.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-18 16:42
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class ListOrderKidsCustom {
    private long money;

    private long moneyPaid;

    private long moneyRemain;

    private List<OrderKidsParentCustom1> dataList;
}
