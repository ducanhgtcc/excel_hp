package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageInClassResponse extends IdResponse {
    private String name;

    private String unit;

    private String category;

    private String type;

    private double price;

    private boolean discount;

    private double discountPrice;

    private String description;

    private boolean used;
}
