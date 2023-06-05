package com.example.onekids_project.response.finance.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-24 16:00
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentCustom1 extends IdResponse {
    private String code;

    private double moneyIn;

    private double moneyOut;
}
