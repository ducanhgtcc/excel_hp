package com.example.onekids_project.mobile.parent.response.finance.walletparent;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-26 09:27
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ListBankParentResponse {
    private String information;

    private List<BankParentResponse> dataList;

}
