package com.example.onekids_project.entity.finance.employeesalary;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-03-03 8:43 SA
 *
 * @author ADMIN
 */

@Getter
@Setter
@Entity
@Table(name = "ex_order_history_employee_salary")
public class ExOrderHistoryEmployeeSalary extends BaseEntity<String> {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_history", nullable = false)
    private OrderEmployeeHistory orderEmployeeHistory;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee_salary", nullable = false)
    private FnEmployeeSalary fnEmployeeSalary;

    private double money;
}
