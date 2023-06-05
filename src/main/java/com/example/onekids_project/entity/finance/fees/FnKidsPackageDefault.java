package com.example.onekids_project.entity.finance.fees;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.feesextend.FnPackageDefaultExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Bảng lưu trữ các khoản thu mặc định của học sinh có thể được lấy từ 2 nguồn:
 * 1, các khoản thu mặc định trong lớp mà hs đang học
 * 2, các khoản thu tự add thêm vào cho học sinh đó(các khoản add vào ngoài các khoản trong đã có trong lớp)
 * có thể cập nhật bất kì khoản thu nào trong này, nhưng chỉ được xóa các khoản thu mà tự add vào cho hs đó
 * nó sẽ lấy những cái có active=true để sinh ra các khoản thu học sinh đã nộp cho tháng sau(tạo cho bảng fn_kids_package).
 * chỉ sinh 1 lần duy nhất
 */
@Getter
@Setter
@Entity
@Table(name = "fn_kids_package_default")
public class FnKidsPackageDefault extends BaseEntity<String> {

    @Column(columnDefinition = "TEXT")
    private String description;

    private int number;

    private double price;

    private boolean discount;

    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean active;

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

    //idClass=0, là thêm từ ngoài vào
    @Column(nullable = false)
    private Long idClass;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_package", nullable = false)
    private FnPackage fnPackage;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kids;

    @JsonBackReference
    @OneToOne(mappedBy = "fnKidsPackageDefault", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private FnPackageDefaultExtend fnPackageDefaultExtend;

}
