package com.example.onekids_project.response.finance;

import com.example.onekids_project.model.finance.NameActiveModel;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-17 14:33
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageCustom2 extends IdResponse {
    private boolean active;

    private String description;

    private boolean approved;

    private boolean locked;

    private int number;

    private double price;

    private boolean discount;

    private double discountNumber;

    private double discountPrice;

    private boolean expired;

    private double paid;

    private PackageCustom5 fnPackage;

    private NameActiveModel fnPackageKidsExtend;
}

@Getter
@Setter
class PackageCustom5 extends IdResponse {
    private String category;

    private String name;

    private String type;

    private String unit;

    private boolean attendance;
}
