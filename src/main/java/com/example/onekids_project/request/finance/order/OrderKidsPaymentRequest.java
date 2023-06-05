package com.example.onekids_project.request.finance.order;

import com.example.onekids_project.common.CycleMoneyConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-02-26 15:59
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class OrderKidsPaymentRequest {
    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private Long idKid;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT, FinanceConstant.CATEGOTY_BOTH})
    private String category;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate date;

    @Min(0)
    //số tiền nhập vào
    private double moneyInput;

    @Min(0)
    //số tiền lấy từ ví
    private double moneyWallet;

    //true: chọn chi tiền vào ví
    private boolean walletStatus;

    private String description;

    //hình thức thanh toán
    @StringInList(values = {FinanceConstant.PAYMENT_CASH, FinanceConstant.PAYMENT_TRANSFER, FinanceConstant.PAYMENT_BOTH})
    private String paymentType;

    @Min(0)
    //tiền mặt
    private double moneyCash;

    @Min(0)
    //chuyển khoản
    private double moneyTransfer;

    @NotBlank
    @StringInList(values = {CycleMoneyConstant.TRANSFER_MONEY_MONTH, CycleMoneyConstant.TRANSFER_MONEY_WALLET})
    private String transferMoneyType;

    @NotEmpty
    private List<Long> idKidsPackageList;
}
