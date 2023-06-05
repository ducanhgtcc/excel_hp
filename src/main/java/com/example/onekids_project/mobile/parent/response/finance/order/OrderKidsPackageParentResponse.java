package com.example.onekids_project.mobile.parent.response.finance.order;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-17 08:46
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsPackageParentResponse {
    private String name;

    private String category;

    private String unit;

    private long price;

    private long discountNumber;

    private String discountType;

    private long discountPrice;

    private int useNumber;

    private long money;

    private long moneyPaid;

    private long moneyRemain;

    //đóng đủ, đóng thiếu, chưa đóng
    private String paidStatus;

}
