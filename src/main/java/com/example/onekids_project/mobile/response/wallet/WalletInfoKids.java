package com.example.onekids_project.mobile.response.wallet;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class WalletInfoKids {
    private String code;

    private long moneyIn;

    private long moneyOut;

    private long moneyRemain;
}
