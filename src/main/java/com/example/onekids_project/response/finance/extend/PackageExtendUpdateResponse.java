package com.example.onekids_project.response.finance.extend;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-01 15:50
 *
 * @author lavanviet
 */
@Getter
@Setter
public class PackageExtendUpdateResponse extends IdResponse {
    private String name;

    private String note;

    private boolean active;

    private List<MoneyExtendResponse> moneyList;
}
