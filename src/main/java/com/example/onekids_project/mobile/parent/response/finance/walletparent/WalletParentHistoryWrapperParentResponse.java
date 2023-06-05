package com.example.onekids_project.mobile.parent.response.finance.walletparent;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-17 13:33
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentHistoryWrapperParentResponse {
    private String month;

    private long moneyInTotal;

    private long moneyOutTotal;

    private List<WalletParentHistoryParentResponse> dataList;
}
