package com.example.onekids_project.response.finance.financegroup;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-01 14:10
 *
 * @author lavanviet
 */
@Getter
@Setter
public class PackageGroupResponse extends IdResponse {
    private String name;

    private String note;

    private int number;
}
