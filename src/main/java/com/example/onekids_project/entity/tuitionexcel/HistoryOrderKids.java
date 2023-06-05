package com.example.onekids_project.entity.tuitionexcel;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.user.MaUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "history_order_kids")
public class HistoryOrderKids extends BaseEntity<String> {
    @Column(name = "collected_money")
    private Double collectedMoney;

    @Column(name = "cash_money")
    private Double cashMoney;

    @Column(name = "transfer_money")
    private Double transferMoney;

    @Column(name = "parent_pay")
    private String parentPay;

    @Column(name = "note_history", length = 1000)
    private String noteHistory;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private MaUser maUser;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t01", nullable = false)
    private OrderKidsExcelT01 orderKidsExcelT01;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t02", nullable = false)
    private OrderKidsExcelT02 orderKidsExcelT02;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t03", nullable = false)
    private OrderKidsExcelT03 orderKidsExcelT03;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t04", nullable = false)
    private OrderKidsExcelT04 orderKidsExcelT04;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t05", nullable = false)
    private OrderKidsExcelT05 orderKidsExcelT05;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t06", nullable = false)
    private OrderKidsExcelT06 orderKidsExcelT06;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t07", nullable = false)
    private OrderKidsExcelT07 orderKidsExcelT07;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t08", nullable = false)
    private OrderKidsExcelT08 orderKidsExcelT08;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t09", nullable = false)
    private OrderKidsExcelT09 orderKidsExcelT09;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t10", nullable = false)
    private OrderKidsExcelT10 orderKidsExcelT10;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t11", nullable = false)
    private OrderKidsExcelT11 orderKidsExcelT11;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids_excel_t12", nullable = false)
    private OrderKidsExcelT12 orderKidsExcelT12;

}
