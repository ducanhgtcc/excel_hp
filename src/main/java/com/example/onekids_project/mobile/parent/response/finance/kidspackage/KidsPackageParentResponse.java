package com.example.onekids_project.mobile.parent.response.finance.kidspackage;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-17 09:54
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageParentResponse extends IdResponse {
    private String name;

    private String category;

    private String unit;

    private long price;

    private long discountNumber;

    private String discountType;
}
