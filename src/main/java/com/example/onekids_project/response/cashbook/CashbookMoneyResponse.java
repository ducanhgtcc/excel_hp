package com.example.onekids_project.response.cashbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashbookMoneyResponse {
    private long moneyIn;

    private long moneyOut;

    private long moneyInOut;
}
