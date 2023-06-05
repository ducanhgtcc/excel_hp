package com.example.onekids_project.response.finance.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-09 09:07
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentStatisticalResponse extends IdResponse {
    private String code;

    private double moneyIn;

    private double moneyOut;

    private String parentName;

    private List<String> kidsNameList;
}
