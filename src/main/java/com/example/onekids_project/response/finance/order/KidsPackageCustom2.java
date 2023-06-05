package com.example.onekids_project.response.finance.order;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-05-10 15:19
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageCustom2 {
    private String name;

    private float number;

    private double price;

    //thành tiền
    private double money;

    //đã trả
    private double paid;

    //còn thiếu
    private double remain;

}
