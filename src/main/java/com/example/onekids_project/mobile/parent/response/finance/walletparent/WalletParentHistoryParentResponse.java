package com.example.onekids_project.mobile.parent.response.finance.walletparent;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-17 13:33
 *
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentHistoryParentResponse extends IdResponse {
    private String date;

    private String category;

    private long money;

    private String description;

    private String pictureLink;

    private boolean status = AppConstant.APP_TRUE;

    private boolean confirm;
}
