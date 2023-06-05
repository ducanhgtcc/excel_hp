package com.example.onekids_project.request.finance.financegroup;

import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-02 10:03
 *
 * @author lavanviet
 */
@Getter
@Setter
public class PackageGroupSearchRequest extends FinanceKidsStatisticalRequest {
    private String name;

    @Override
    public String toString() {
        return "PackageGroupSearchRequest{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
