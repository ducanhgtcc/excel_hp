package com.example.onekids_project.response.finance;

import com.example.onekids_project.model.finance.NameActiveModel;
import com.example.onekids_project.response.base.IdResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageDefaultDetailResponse extends IdResponse {
    private String description;

    private int number;

    private double price;

    private boolean discount;

    private double discountNumber;

    private double discountPrice;

    private boolean expired;

    private boolean active;

    private boolean existKidsPackage;

    private Long idClass;

    private PackageCustom1 fnPackage;

    private NameActiveModel fnPackageDefaultExtend;
}

