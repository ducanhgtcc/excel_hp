package com.example.onekids_project.response.cashbook;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-03-09 15:22
 *
 * @author lavanviet
 */
@Getter
@Setter
public class CashBookResponse extends IdResponse {
    private int year;

    private boolean locked;

    private LocalDate startDate;

    private LocalDate endDate;

    private double moneyIn;

    private double moneyOut;

    private double moneyStart;
}
