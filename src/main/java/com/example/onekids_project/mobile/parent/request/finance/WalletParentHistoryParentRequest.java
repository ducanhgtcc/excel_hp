package com.example.onekids_project.mobile.parent.request.finance;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * date 2021-03-17 13:46
 * 
 * @author lavanviet
 */
@Data
public class WalletParentHistoryParentRequest {
    @NotNull
    private Integer year;

    private String description;
}
