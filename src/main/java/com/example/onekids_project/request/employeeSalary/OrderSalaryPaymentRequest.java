package com.example.onekids_project.request.employeeSalary;

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
 * date 2021-03-03 4:08 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
@ToString
public class OrderSalaryPaymentRequest {

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private Long idInfoEmployee;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT, FinanceConstant.CATEGOTY_BOTH})
    private String category;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate date;

    //số tiền nhập vào
    private double moneyInput;

    private String description;

    @NotEmpty
    private List<Long> idEmployeeSalaryList;
}
