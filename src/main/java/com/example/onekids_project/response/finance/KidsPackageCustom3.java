package com.example.onekids_project.response.finance;

import com.example.onekids_project.model.finance.NameActiveModel;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-18 13:17
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageCustom3 extends IdResponse {

    private String description;

    private boolean approved;

    private boolean locked;

    private int number;

    private double price;

    private boolean discount;

    private double discountNumber;

    private double discountPrice;

    private boolean expired;

    private int usedNumber;

    private double paid;

    //= usedNumber khi lấy ra
    private int showNumber;

    //thuộc tính tự bổ sung
    //số lượng tính toán: khi áp dụng điểm danh
    private int calculateNumber;

    //tiền dự trên số lượng tính toán
    private double moneyTemp;

    //số tiền thực tế phải trả
    private long money;

    //số tiền tăng/giảm từ khoản đính kèm
    private long moneyExtend;

    private PackageCustom6 fnPackage;

    private NameActiveModel fnPackageKidsExtend;
}

@Getter
@Setter
class PackageCustom6 extends IdResponse {

    private String category;

    private String name;

    private String type;

    private String unit;

    private boolean attendance;
}
