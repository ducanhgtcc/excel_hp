package com.example.onekids_project.request.finance.order;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-02-26 10:39
 *
 * @author lavanviet
 */
@Data
public class OrderRequest {
    @NotNull
    private Long idKid;

//    @NotNull
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate date;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT, FinanceConstant.CATEGOTY_BOTH})
    private String category;
}
