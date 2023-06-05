package com.example.onekids_project.response.finance.wallet;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-09 10:12
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ListWalletParentStatisticalResponse extends TotalResponse {
    private double moneyInTotal;

    private double moneyOutTotal;

    private List<WalletParentStatisticalResponse> dataList;
}
