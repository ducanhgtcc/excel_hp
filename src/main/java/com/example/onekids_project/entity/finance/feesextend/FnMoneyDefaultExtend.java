package com.example.onekids_project.entity.finance.feesextend;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-09-21 15:36
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "fn_money_default_extend")
public class FnMoneyDefaultExtend extends BaseEntity<String> {
    //increase, reduce
    @Column(nullable = false)
    private String category1;

    //fixed, dynamic
    private String category2;

    //percent, money
    private String typeFixed;

    //số tiền hoặc phần trăm
    private long fixedData;

    //absentAll, absentDetail
    private String category3;

    //percent, number
    private String typeDynamic;

    //percent, money
    private String typeDiscount;

    //khoảng cho nghỉ chung
    private String rangeALl;

    //khoảng nghỉ có phép
    private String rangeAbsentYes;

    //khoảng nghỉ không phép
    private String rangeAbsentNo;

    //khoảng chưa điểm danh
    private String rangeNoAttendance;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_package_default_extend", nullable = false)
    private FnPackageDefaultExtend fnPackageDefaultExtend;

}
