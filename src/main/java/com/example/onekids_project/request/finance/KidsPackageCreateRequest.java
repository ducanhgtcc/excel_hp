package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class KidsPackageCreateRequest extends DateNotNullRequest {
    @NotNull
    private Long idPackage;

    private String description;

    private int number;

    private double price;

    private boolean discount;

    @StringInList(values = {FinanceConstant.DISCOUNT_TYPE_PERCENT, FinanceConstant.DISCOUNT_TYPE_AMOUNT})
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

    @Override
    public String toString() {
        return "KidsPackageCreateRequest{" +
                "idPackage=" + idPackage +
                ", description='" + description + '\'' +
                ", number=" + number +
                ", price=" + price +
                ", discount=" + discount +
                ", discountType='" + discountType + '\'' +
                ", discountNumber=" + discountNumber +
                ", discountPrice=" + discountPrice +
                ", active=" + active +
                ", expired=" + expired +
                ", febNumberExpired=" + febNumberExpired +
                ", smallNumberExpired=" + smallNumberExpired +
                ", largeNumberExpired=" + largeNumberExpired +
                ", monthNumber=" + monthNumber +
                ", endDateExpired=" + endDateExpired +
                "} " + super.toString();
    }
}
