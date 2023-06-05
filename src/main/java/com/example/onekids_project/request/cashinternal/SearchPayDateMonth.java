package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-07-27 10:23 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@ToString
public class SearchPayDateMonth {

    @StringInList(values = {FinanceConstant.TYPE_MONTH, FinanceConstant.TYPE_DATE})
    private String type;
    @StringInList(values = {FinanceConstant.CATEGORY_OUT, FinanceConstant.CATEGORY_IN})
    private String category;

    @Min(1)
    @Max(12)
    private Integer month;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;

}
