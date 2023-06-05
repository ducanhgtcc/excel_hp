package com.example.onekids_project.mobile.parent.response.finance.kidspackage;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-17 11:00
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageDetailParentResponse {
    private String name;

    private String category;

    private int number;

    private String unit;

    private long price;

    private long discountNumber;

    private String discountType;

    private long discountPrice;

    private String expireDate;

    private String note;
}
