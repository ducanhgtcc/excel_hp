package com.example.onekids_project.mobile.parent.response.finance.kidspackage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-17 09:57
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageWrapperParentResponse {
    private String month;

    private List<KidsPackageParentResponse> dataList;
}
