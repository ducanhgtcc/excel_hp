package com.example.onekids_project.entity.employee;


import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "employee_notify")
public class EmployeeNotify extends BaseEntity<String> {

    @Column(columnDefinition = "bit default 1")
    private boolean message = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean medicine = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean absent = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean feedback = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean sys = AppConstant.APP_TRUE;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_info_employee", nullable = false, unique = true)
    private InfoEmployeeSchool infoEmployeeSchool;

}
