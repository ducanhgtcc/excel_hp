package com.example.onekids_project.request.finance.wallet;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-25 15:36
 *
 * @author lavanviet
 */
@Getter
@Setter
public class SearchWalletParentHistoryRequest extends PageNumberWebRequest {
    @NotNull
    private Long idWalletParent;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;

    @StringInList(values = {FinanceConstant.CATEGORY_IN,FinanceConstant.CATEGORY_OUT})
    private String category;

    private String type;

    private Boolean status;

    @Override
    public String toString() {
        return "SearchWalletParentHistoryRequest{" +
                "idWalletParent=" + idWalletParent +
                ", dateList=" + dateList +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}
