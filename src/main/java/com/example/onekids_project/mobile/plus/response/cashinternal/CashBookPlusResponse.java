package com.example.onekids_project.mobile.plus.response.cashinternal;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-06-18 15:28
 *
 * @author lavanviet
 */
@Getter
@Setter
public class CashBookPlusResponse {
    private String time;

    private long moneyIn;

    private long moneyOut;

    private long moneyInOut;

    private long moneyStart;

    private long moneyEnd;

}
