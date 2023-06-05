package com.example.onekids_project.mobile.parent.response.finance.walletparent;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-17 15:32
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentParentResponse {
    private String avatar;

    private String code;

    private long moneyIn;

    private long moneyOut;

    private long moneyRemain;

    private String fullName;

    private String address;

    private String phone;
}
