package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-04-05 13:21
 *
 * @author lavanviet
 */
@Getter
@Setter
public class FnSalarySampleBriefResponse extends IdResponse {
    private String name;

    private String description;

    private String category;

    private String unit;

    private double price;

    private boolean discount;

    private double discountPrice;

    private boolean attendance;
}
