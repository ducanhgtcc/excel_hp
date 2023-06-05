package com.example.onekids_project.response.finance.fnpackage;

import com.example.onekids_project.model.common.NameModel;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageRootResponse extends IdResponse {
    private String name;

    private String category;

    private String type;

    private String description;

    private String unit;

    private int number;

    private double price;

    private boolean discount;

    private double discountNumber;

    private double discountPrice;

    private boolean expired;

    private boolean active;

    private boolean attendance;

    private NameModel fnPackageExtend;
}
