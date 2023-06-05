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
public class KidsPackageForPaymentResponse extends IdResponse {
    private boolean locked;

    private double paid;

    //(số lượng sử dụng*đơn giá)/số lượng mỗi đơn giá
    private double money;

    //dùng để check tính toán ra số tiền trên giao diện
    private boolean checkMoney;

    //dùng để tích chọn thanh toán trên giao diện
    private boolean checked;

    private PackageCustom7 fnPackage;
}

@Getter
@Setter
class PackageCustom7 {
    private String name;

    private String unit;

    private String type;

    private String category;
}
