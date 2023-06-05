package com.example.onekids_project.entity.finance.fees;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * bảng chứa các khoản thu của một trường
 */
@Getter
@Setter
@Entity
@Table(name = "fn_package")
public class FnPackage extends BaseEntity<String> {

    @Column(nullable = false)
    private String name;

    // in, out
    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String unit;

    private int number;

    private double price;

    private boolean discount;

    //FinanceConstant: percent, money
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean active;

    private boolean attendance;

    //FinanceConstant: before(trả trước), after(trả sau)//sau này bỏ default đi
    @Column(columnDefinition = "varchar(255) default 'after'")
    private String attendancePaid;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;

    //single, multiple
    @Column(nullable = false)
    private String type;

    private boolean t1;

    private boolean t2;

    private boolean t3;

    private boolean t4;

    private boolean t5;

    private boolean t6;

    private boolean t7;

    private boolean t8;

    private boolean t9;

    private boolean t10;

    private boolean t11;

    private boolean t12;

    //true là hết hạn, false là ko hết hạn
    private boolean expired;

    //    [1-28]
    private int febNumberExpired;

    //    [1-30]
    private int smallNumberExpired;

    //    [1-31]
    private int largeNumberExpired;

    //0 là tháng hiện tại, 1 là tháng kế tiếp với kiểu nhiều lần
    //[0-1]
    private int monthNumber;

    //ngày hết hạn với kiểu 1 lần
    private LocalDate endDateExpired;

    private long sortNumber;

    //trạng thái đang sử dụng: true->đang sử dụng
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean usingStatus = true;

    //khoản hệ thống: true
    private boolean rootStatus;

    private int rootNumber;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToMany(mappedBy = "fnPackageSet", fetch = FetchType.LAZY)
    Set<MaClass> maClassSet;

    @JsonBackReference
    @OneToMany(mappedBy = "fnPackage", fetch = FetchType.LAZY)
    List<FnKidsPackageDefault> fnKidsPackageDefaultList;

    @JsonBackReference
    @OneToMany(mappedBy = "fnPackage", fetch = FetchType.LAZY)
    List<FnKidsPackage> fnKidsPackageList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_package_group")
    private FnPackageGroup fnPackageGroup;

    @JsonBackReference
    @OneToOne(mappedBy = "fnPackage", fetch = FetchType.LAZY)
    private FnPackageExtend fnPackageExtend;

}
