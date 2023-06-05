package com.example.onekids_project.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-26 10:41
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageForPaymentDetailResponse extends IdResponse {
    private boolean locked;

    private double paid;

    //(số lượng sử dụng*đơn giá)/số lượng mỗi đơn giá
    private double money;

    private PackageCustom8 fnPackage;
}

@Getter
@Setter
class PackageCustom8 {
    private String name;

    private String unit;

    private String type;

    private String category;
}

