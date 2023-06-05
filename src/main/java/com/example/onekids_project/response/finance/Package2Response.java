package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
public class Package2Response extends IdResponse {
    private String name;

    private String category;

    private String description;

    private String unit;

    private int number;

    private double price;

    private boolean discount;

    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean active;

    private boolean attendance;

    private String attendancePaid;

    private String attendanceType;

    private String attendanceDetail;

    //single, multiple
    @Column(nullable = false)
    private String type;

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

    //true là hết hạn, false là ko hết hạn
    private boolean expired;

    //    [1-28]
    private int febNumberExpired;

    //    [1-30]
    private int smallNumberExpired;

    //    [1-31]
    private int largeNumberExpired;

    //0 là tháng hiện tại, 1 là tháng kế tiếp với kiểu nhiều lần
    //[0-1]
    private int monthNumber;

    //ngày hết hạn với kiểu 1 lần
    private LocalDate endDateExpired;
}
