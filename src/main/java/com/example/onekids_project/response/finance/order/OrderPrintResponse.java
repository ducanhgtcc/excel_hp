package com.example.onekids_project.response.finance.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

/**
 * date 2021-05-10 14:52
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderPrintResponse {
    private String fullName;

    private String className;

    private String phone;

    private String schoolName;

    private String schoolAddress;

    private String fullNameBank;

    private String accountNumberBank;

    private String bankNameBank;

    private String bankInfo;

    private String userCreate;

    //avatar school
    private String avatar;

    private List<KidsPackageCustom2> dataList;

}
