package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-02-23 08:31
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "fn_cash_book")
public class FnCashBook extends BaseEntity<String> {
    private int year;

    //khóa là ko thể thực hiện các thao tác thanh toán trong năm đó
    private boolean locked;

    private Long idLocked;

    private LocalDateTime timeLocked;

    //ngày tạo trường hoặc ngày đầu năm
    @Column(nullable = false)
    private LocalDate startDate;

    //ngày cuối năm(31-12)
    @Column(nullable = false)
    private LocalDate endDate;

    //tăng trong kỳ
    private double moneyIn;

    //giảm trong kỳ
    private double moneyOut;

    //số dư đầu kỳ
    private double moneyStart;

    @JsonManagedReference
    @OneToMany(mappedBy = "fnCashBook", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CashBookHistory> cashBookHistoryList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false)
    private School school;

}
