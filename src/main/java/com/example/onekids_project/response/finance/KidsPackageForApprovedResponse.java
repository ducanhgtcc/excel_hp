package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-18 13:14
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageForApprovedResponse extends IdResponse {
    private String fullName;

    private LocalDate birthDay;

    //còn lại phải thu: bằng hiệu còn lại thu-còn lại chi>0
    private double totalMoneyRemainIn;

    //còn lại phải chi: bằng hiệu còn lại thu-còn lại chi<0
    private double totalMoneyRemainOut;

    private double totalMoneyIn;

    private double totalMoneyOut;

    private String approvedNumber;

    private String lockedNumber;

    private List<KidsPackageCustom3> fnKidsPackageList;
}
