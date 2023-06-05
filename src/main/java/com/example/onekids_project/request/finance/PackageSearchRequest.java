package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

@Data
public class PackageSearchRequest {
    private String name;

    private Boolean usingStatus;

    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT})
    private String category;
}
