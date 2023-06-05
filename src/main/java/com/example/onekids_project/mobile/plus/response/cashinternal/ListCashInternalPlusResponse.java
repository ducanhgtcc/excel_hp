package com.example.onekids_project.mobile.plus.response.cashinternal;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-06-18 14:12
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ListCashInternalPlusResponse extends LastPageBase {
    private List<CashInternalPlusResponse> dataList;
}
