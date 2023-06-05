package com.example.onekids_project.entity.finance.employeesalary;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "fn_employee_salary_default")
public class FnEmployeeSalaryDefault extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;

    // in , out
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

    // được dăng áp dụng: true sinh ra khoản công lương cho nhân sự, false là ko sinh ra
    private boolean active;

    private boolean attendance;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;

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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

    @JsonManagedReference
    @OneToMany(mappedBy = "fnEmployeeSalaryDefault", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FnEmployeeSalary> fnEmployeeSalaryList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fn_salary")
    private FnSalary fnSalary;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_salary_group")
    private FnSalaryGroup fnSalaryGroup;
}
