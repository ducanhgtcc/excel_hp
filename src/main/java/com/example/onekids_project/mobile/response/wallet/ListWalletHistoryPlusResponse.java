package com.example.onekids_project.mobile.response.wallet;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class ListWalletHistoryPlusResponse extends LastPageBase {
    private List<WalletHistoryPlusResponse> dataList;
}
