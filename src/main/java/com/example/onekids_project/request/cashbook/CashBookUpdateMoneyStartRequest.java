package com.example.onekids_project.request.cashbook;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * date 2021-03-09 14:48
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class CashBookUpdateMoneyStartRequest extends IdRequest {
    @NotNull
    private Double moneyStart;

    @Override
    public String toString() {
        return "CashBookUpdateMoneyStartRequest{" +
                "moneyStart=" + moneyStart +
                "} " + super.toString();
    }
}
