package com.example.onekids_project.request.finance.financegroup;

import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * date 2021-06-04 13:47
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ExportRequest extends FinanceKidsStatisticalRequest {
    @NotEmpty
    private List<Long> idGroupList;

    @Override
    public String toString() {
        return "ExportRequest{" +
                "idGroupList=" + idGroupList +
                "} " + super.toString();
    }
}
