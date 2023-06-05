package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KidsPackageUpdateResponse extends IdResponse {
    private String description;

    private int number;

    private double price;

    private boolean discount;

    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean active;

    private boolean expired;

    private int febNumberExpired;

    private int smallNumberExpired;

    private int largeNumberExpired;

    private int monthNumber;

    private LocalDate endDateExpired;

    private PackageCustom4 fnPackage;
}

@Getter
@Setter
class PackageCustom4 {
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
