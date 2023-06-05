package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PackageDefaultUpdateResponse extends IdResponse {
    private String description;

    private int number;

    private double price;

    private boolean discount;

    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean active;

    private boolean t1;

    private boolean t2;

    private boolean t3;

    private boolean t4;

    private boolean t5;

    private boolean t6;

    private boolean t7;

    private boolean t8;

    private boolean t9;

    private boolean t10;

    private boolean t11;

    private boolean t12;

    private boolean expired;

    private int febNumberExpired;

    private int smallNumberExpired;

    private int largeNumberExpired;

    private int monthNumber;

    private LocalDate endDateExpired;

    private PackageCustom2 fnPackage;
}

@Getter
@Setter
class PackageCustom2 {
    private Long id;

    private String name;

    private String category;

    private String type;

    private String unit;

    private boolean attendance;

    private String attendancePaid;

    private String attendanceType;

    private String attendanceDetail;

}
