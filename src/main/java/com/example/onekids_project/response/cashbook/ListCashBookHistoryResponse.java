package com.example.onekids_project.response.cashbook;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-10 09:44
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class ListCashBookHistoryResponse  extends TotalResponse {
    private double moneyIn;

    private double moneyOut;

    private double moneyStart;

    private double moneyEnd;

    private List<CashBookHistoryResponse> dataList;
}
