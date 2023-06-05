package com.example.onekids_project.entity.finance.employeesalary;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-24 9:17 SA
 *
 * @author ADMIN
 */

@Getter
@Setter
@Entity
@Table(name = "order_employee_history")
public class OrderEmployeeHistory extends BaseEntity<String> {
    //nạp tiền->in, rút tiền->out
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    //số tiền nhập vào từ giao diện
    private double moneyInput;

    //số tiền đã thanh toán : xoa tren servertest
//    private double moneyPaid;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_employee", nullable = false)
    private FnOrderEmployee fnOrderEmployee;

    @JsonManagedReference
    @OneToMany(mappedBy = "orderEmployeeHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ExOrderHistoryEmployeeSalary> exOrderHistoryEmployeeSalaryList;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_cash_book_history", nullable = false)
    private CashBookHistory cashBookHistory;
}
