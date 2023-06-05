package com.example.onekids_project.mobile.plus.request.cashinternal;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * date 2021-06-22 10:09
 *
 * @author lavanviet
 */
@Getter
@Setter
public class CashBookPlusRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @StringInList(values = {FinanceConstant.CASH_BOOK_KID, FinanceConstant.CASH_BOOK_EMP, FinanceConstant.CASH_BOOK_SCH})
    private String type;
}
