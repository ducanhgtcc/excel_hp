package com.example.onekids_project.mobile.response.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-22 08:38
 *
 * @author lavanviet
 */
@Getter
@Setter
public class WalletClassPlusResponse extends IdResponse {
    private String className;

    private int schoolNoConfirm;

    private int parentNoConfirm;
}
