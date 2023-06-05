package com.example.onekids_project.request.finance.financegroup;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-06-03 12:05
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class CashBookMoneyRequest {
    @NotNull
    private Integer year;

    @StringInList(values = {FinanceConstant.TYPE_MONTH, FinanceConstant.TYPE_DATE})
    private String type;

    @Min(1)
    @Max(12)
    private Integer month;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;
}
