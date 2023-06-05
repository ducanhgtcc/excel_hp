package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PackageForDefaultAddResponse extends IdResponse {
    private String name;

    private String description;

    private String unit;

    private int number;

    private double price;

    private boolean discount;

    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean expired;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean enough;

    private boolean active;

    private boolean attendance;

    private String attendanceType;

    private String attendanceDetail;
}
