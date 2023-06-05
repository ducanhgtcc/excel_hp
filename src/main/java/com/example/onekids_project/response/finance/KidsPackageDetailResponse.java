package com.example.onekids_project.response.finance;

import com.example.onekids_project.model.finance.NameActiveModel;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsPackageDetailResponse extends IdResponse {
    private String description;

    private int number;

    private double price;

    private boolean discount;

    private double discountNumber;

    private double discountPrice;

    private boolean expired;

    private boolean active;

    private boolean approved;

    private boolean locked;

    private double paid;

    //true: khoản trong lớp, false: khoản khác
    private boolean defaultStatus;

    private PackageCustom3 fnPackage;

    private NameActiveModel fnPackageKidsExtend;

}


