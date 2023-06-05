package com.example.onekids_project.response.finance.financegroup;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-01 16:56
 *
 * @author lavanviet
 */
@Getter
@Setter
public class FnKidsPackageForGroupResponse extends IdResponse {
    private String name;

    private String category;

    private String unit;

    private String description;
}
