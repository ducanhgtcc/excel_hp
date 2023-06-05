package com.example.onekids_project.entity.finance.employeesalary;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-06-01 13:54
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "fn_salary_group")
public class FnSalaryGroup extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;

    private String note;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @OneToMany(mappedBy = "fnSalaryGroup", fetch = FetchType.LAZY)
    List<FnSalary> fnSalaryList;

    @JsonBackReference
    @OneToMany(mappedBy = "fnSalaryGroup", fetch = FetchType.LAZY)
    List<FnEmployeeSalaryDefault> fnEmployeeSalaryDefaultList;

    @JsonBackReference
    @OneToMany(mappedBy = "fnSalaryGroup", fetch = FetchType.LAZY)
    List<FnEmployeeSalary> fnEmployeeSalaryList;

}
