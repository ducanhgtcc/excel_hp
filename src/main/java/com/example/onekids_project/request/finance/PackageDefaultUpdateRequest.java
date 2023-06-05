package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class PackageDefaultUpdateRequest extends IdRequest {
    private String description;

    private int number;

    private double price;

    private boolean discount;

    @StringInList(values = {FinanceConstant.DISCOUNT_TYPE_PERCENT, FinanceConstant.DISCOUNT_TYPE_AMOUNT})
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

    @Min(0)
    @Max(28)
    private int febNumberExpired;

    @Min(0)
    @Max(30)
    private int smallNumberExpired;

    @Min(0)
    @Max(31)
    private int largeNumberExpired;

    @Min(0)
    @Max(1)
    private int monthNumber;

    private LocalDate endDateExpired;

    @Override
    public String toString() {
        return "PackageDefaultUpdateRequest{" +
                "description='" + description + '\'' +
                ", number=" + number +
                ", price=" + price +
                ", discount=" + discount +
                ", discountType='" + discountType + '\'' +
                ", discountNumber=" + discountNumber +
                ", discountPrice=" + discountPrice +
                ", expired=" + expired +
                ", active=" + active +
                "} " + super.toString();
    }
}
