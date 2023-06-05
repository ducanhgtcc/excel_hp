package com.example.onekids_project.request.finance.wallet;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-09 09:12
 *
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentStatisticalRequest extends PageNumberWebRequest {
    private String code;

    @StringInList(values = {FinanceConstant.TYPE_POSITIVE, FinanceConstant.TYPE_ZERO})
    private String type;

    private String kidName;

    @Override
    public String toString() {
        return "WalletParentStatisticalRequest{" +
                "code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", kidName='" + kidName + '\'' +
                "} " + super.toString();
    }
}
