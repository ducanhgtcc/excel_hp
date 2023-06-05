package com.example.onekids_project.entity.finance.fees;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.feesextend.FnPackageDefaultExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageKidsExtend;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * bảng chứa các khoản thu của học sinh
 */
@Getter
@Setter
@Entity
@Table(name = "fn_kids_package")
public class FnKidsPackage extends BaseEntity<String> {
    private int month;

    private int year;

    private boolean locked;

    private Long idLocked;

    private LocalDateTime timeLocked;

    private boolean approved;

    private Long idApproved;

    private LocalDateTime timeApproved;

    private boolean showParent;

    private boolean showTeacher;

    //số lượng sử dụng thực tế
    private int usedNumber;

    //số tiền đã thanh toán
    private double paid;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int number;

    private double price;

    private boolean discount;

    //money, percent
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    //được đăng ký để có thể vào duyệt
    private boolean active;

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

    //thuộc tính để test
    private String note;

    //khoản hệ thống: true
    private boolean rootStatus;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_package", nullable = false)
    private FnPackage fnPackage;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kids;

    @JsonManagedReference
    @OneToMany(mappedBy = "fnKidsPackage", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ExOrderHistoryKidsPackage> exOrderHistoryKidsPackageList;

    @JsonBackReference
    @OneToOne(mappedBy = "fnKidsPackage", fetch = FetchType.LAZY)
    private FnPackageKidsExtend fnPackageKidsExtend;

}
