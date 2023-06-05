package com.example.onekids_project.request.finance.extend;

import com.example.onekids_project.common.PackageExtendConstant;
import com.example.onekids_project.util.objectdata.RangeExtendModel;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * date 2021-10-01 15:52
 *
 * @author lavanviet
 */
@Getter
@Setter
public class MoneyCreateExtendRequest {
    //increase, reduce
    @NotBlank
    @StringInList(values = {PackageExtendConstant.INCREASE, PackageExtendConstant.REDUCE})
    private String category1;

    //fixed, dynamic
    @StringInList(values = {PackageExtendConstant.FIXED, PackageExtendConstant.DYNAMIC})
    private String category2;

    //percent, money
    @StringInList(values = {PackageExtendConstant.PERCENT, PackageExtendConstant.MONEY})
    private String typeFixed;

    //số tiền hoặc phần trăm
    private long fixedData;

    //absentAll, absentDetail
    @StringInList(values = {PackageExtendConstant.ABSENT_ALL, PackageExtendConstant.ABSENT_DETAIL})
    private String category3;

    //percent, number
    @StringInList(values = {PackageExtendConstant.PERCENT, PackageExtendConstant.NUMBER})
    private String typeDynamic;

    //percent, money
    @StringInList(values = {PackageExtendConstant.PERCENT, PackageExtendConstant.MONEY})
    private String typeDiscount;

    //khoảng cho nghỉ chung
    private List<RangeExtendModel> dataList;

    //khoảng nghỉ có phép
    private List<RangeExtendModel> absentYesList;

    //khoảng nghỉ không phép
    private List<RangeExtendModel> absentNoList;

    //khoảng chưa điểm danh
    private List<RangeExtendModel> noAttendanceList;
}
