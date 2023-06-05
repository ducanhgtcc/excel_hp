package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityNoAuditing;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employee_status_timeline")
public class EmployeeStatusTimeline extends BaseEntity<String> {
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    //trạng thái của nhân viên: EmployeeConstant
    @Column(nullable = false)
    private String status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

}
