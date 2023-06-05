package com.example.onekids_project.mobile.response.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-22 09:24
 *
 * @author lavanviet
 */
@Getter
@Setter
public class WalletKidsPlusResponse extends IdResponse {
    private String avatar;

    private String fullName;

    private String className;

    private Long idWallet;

    private String codeWallet;

    private int schoolNoConfirm;

    private int parentNoConfirm;

    //số dư ví
    private long money;

}
