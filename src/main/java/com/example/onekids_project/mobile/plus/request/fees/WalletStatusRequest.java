package com.example.onekids_project.mobile.plus.request.fees;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class WalletStatusRequest extends PageNumberRequest {

    @StringInList(values = {FinanceConstant.WALLET_UNCONFIRM_SCHOOL, FinanceConstant.WALLET_UNCONFIRM_PARENT, FinanceConstant.WALLET_UNCONFIRM, FinanceConstant.WALLET_CONFIRM})
    private String status;
}
