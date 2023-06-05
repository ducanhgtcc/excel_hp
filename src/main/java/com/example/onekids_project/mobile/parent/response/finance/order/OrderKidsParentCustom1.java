package com.example.onekids_project.mobile.parent.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-16 13:27
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsParentCustom1 extends IdResponse {
    private String name;

    private String category;

    //đóng đủ, đóng thiếu, chưa đóng
    private String paidStatus;

    private long money;

    private long moneyPaid;

    private long moneyRemain;
}
