package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ex_department_employee")
public class ExDepartmentEmployee extends BaseEntity<String> {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_department")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

    private String position;
}
