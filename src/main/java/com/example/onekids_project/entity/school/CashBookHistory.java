package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.entity.finance.employeesalary.OrderEmployeeHistory;
import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * date 2021-02-23 14:21
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "cash_book_history")
public class CashBookHistory extends BaseEntity<String> {
    //in, out
    @Column(nullable = false)
    private String category;

    //học sinh(KID), nhân sự(EMP), thu chi nội bộ(SCH): in FinanceConstant
    @Column(nullable = false)
    private String type;

    private double money;

    @Column(nullable = false)
    private LocalDate date;

    //UNAPPROVED, DELETE
    private String origin;

    @JsonManagedReference
    @OneToOne(mappedBy = "cashBookHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private OrderKidsHistory orderKidsHistory;

    @JsonManagedReference
    @OneToOne(mappedBy = "cashBookHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private OrderEmployeeHistory orderEmployeeHistory;

    @JsonManagedReference
    @OneToOne(mappedBy = "cashBookHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private FnCashInternal fnCashInternal;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cash_book", nullable = false)
    private FnCashBook fnCashBook;

    private String identify;

}
