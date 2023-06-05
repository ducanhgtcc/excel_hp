package com.example.onekids_project.entity.finance.fees;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-22 16:55
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "order_kids_history")
public class OrderKidsHistory extends BaseEntity<String> {

    //nạp tiền->in, rút tiền->out
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    //số tiền nhập vào từ giao diện
    private double moneyInput;

    //số tiền đã thanh toán từ ví
    private double moneyWallet;

    //hình thức thanh toán: CASH, TRANSFER, BOTH
    private String paymentType;

    //tiền mặt
    private Double moneyCash;

    //chuyển khoản
    private Double moneyTransfer;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_kids", nullable = false)
    private FnOrderKids fnOrderKids;

    @JsonManagedReference
    @OneToMany(mappedBy = "orderKidsHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ExOrderHistoryKidsPackage> exOrderHistoryKidsPackageList;

    @JsonManagedReference
    @OneToMany(mappedBy = "orderKidsHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<WalletParentHistory> walletParentHistoryList;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_cash_book_history", nullable = false)
    private CashBookHistory cashBookHistory;
}
