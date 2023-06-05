package com.example.onekids_project.response.finance.wallet;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-13 13:54
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ListWalletParentHistoryResponse extends TotalResponse {
    private List<WalletParentHistoryResponse> dataList;
}
