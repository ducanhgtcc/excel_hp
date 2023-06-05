package com.example.onekids_project.entity.finance.employeesalary;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.fees.FnPackageGroup;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "fn_employee_salary")
public class FnEmployeeSalary extends BaseEntity<String> {

    private int year;

    private int month;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean showing = AppConstant.APP_TRUE;

    private boolean approved;

    private Long idApproved;

    private LocalDateTime timeApproved;

    private boolean locked;

    private Long idLocked;

    private LocalDateTime timeLocked;

    private double paid;

    @Column(nullable = false)
    private String name;

    // in , out
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String unit;

    private String description;

    private int number;

    private float userNumber;

    private double price;

    private boolean discount;

    private boolean attendance;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;

    //FinanceConstant: percent, money
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee_salary_default")
    private FnEmployeeSalaryDefault fnEmployeeSalaryDefault;

    @JsonManagedReference
    @OneToMany(mappedBy = "fnEmployeeSalary", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ExOrderHistoryEmployeeSalary> exOrderHistoryEmployeeSalaryList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_salary_group")
    private FnSalaryGroup fnSalaryGroup;

}
