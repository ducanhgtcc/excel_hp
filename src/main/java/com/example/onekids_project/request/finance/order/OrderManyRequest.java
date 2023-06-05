package com.example.onekids_project.request.finance.order;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lavanviet
 */
@Data
public class OrderManyRequest {
    @NotNull
    private Long idKid;

    @NotNull
    private Long idOrder;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT, FinanceConstant.CATEGOTY_BOTH})
    private String category;
}
