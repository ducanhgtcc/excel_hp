package com.example.onekids_project.response.finance.extend;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.util.objectdata.RangeExtendModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-01 15:52
 *
 * @author lavanviet
 */
@Getter
@Setter
public class MoneyExtendResponse extends IdResponse {
    private String category1;

    private String category2;

    private String typeFixed;

    private long fixedData;

    private String category3;

    private String typeDynamic;

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
