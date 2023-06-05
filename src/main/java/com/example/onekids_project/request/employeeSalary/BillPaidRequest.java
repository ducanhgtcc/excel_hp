package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-03-01 10:44 SA
 *
 * @author ADMIN
 */

@Getter
@Setter
@ToString
public class BillPaidRequest {
    @NotNull
    private Long idInfoEmployee;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGOTY_BOTH, FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT})
    private String category;

}
