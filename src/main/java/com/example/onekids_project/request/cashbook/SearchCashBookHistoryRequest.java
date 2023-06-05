package com.example.onekids_project.request.cashbook;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-09 16:38
 *
 * @author lavanviet
 */
@Getter
@Setter
public class SearchCashBookHistoryRequest extends PageNumberWebRequest {
    @NotNull
    private Long idCashBook;

    @StringInList(values = {FinanceConstant.TYPE_MONTH, FinanceConstant.TYPE_DATE})
    private String type;

    @Min(1)
    @Max(12)
    private Integer month;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;

    @StringInList(values = {FinanceConstant.CASH_BOOK_KID, FinanceConstant.CASH_BOOK_EMP, FinanceConstant.CASH_BOOK_SCH})
    private String typeCashbook;

    @Override
    public String toString() {
        return "SearchCashBookHistoryRequest{" +
                "idCashBook=" + idCashBook +
                ", type='" + type + '\'' +
                ", month=" + month +
                ", dateList=" + dateList +
                "} " + super.toString();
    }
}
