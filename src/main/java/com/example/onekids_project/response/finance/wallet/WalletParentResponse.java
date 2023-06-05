package com.example.onekids_project.response.finance.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-02-23 15:29
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentResponse extends IdResponse {
    private String fullName;

    private LocalDate birthDay;

    private String className;

    private long numberStatus;

    private WalletParentCustom1 walletParent;

}
