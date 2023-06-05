package com.example.onekids_project.entity.finance.CashInternal;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * bảng chứa các phiếu thu hoặc chi
 */
@Getter
@Setter
@Entity
@Table(name = "fn_cash_internal")
public class FnCashInternal extends BaseEntity<String> {

    // PC - PT + idSchool - id++
    @Column(nullable = false, unique = true)
    private String code;

    private double money;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String content;

    // in, out
    @Column(nullable = false)
    private String category;

    // true là đã thanh toán
    private boolean payment;

    // true là duyệt
    private boolean approved;

    //true là đã hủy
    // chỉ hủy khi chưa thanh toán -  nếu hủy rồi thì chỉ show lên k thao tác gì nữa
    private boolean canceled;

    private Long idApproved;

    private LocalDateTime timeApproved;

    private Long idPayment;

    // thời gian thanh toán
    private LocalDateTime timePayment;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_people_type_internal", nullable = false)
    private FnPeopleType fnPeopleTypeInternal;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_people_type_other", nullable = false)
    private FnPeopleType fnPeopleTypeOther;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_cash_book_history")
    private CashBookHistory cashBookHistory;
}
