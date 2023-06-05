package com.example.onekids_project.entity.finance.employeesalary;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-02-24 9:14 SA
 *
 * @author ADMIN
 */

@Getter
@Setter
@Entity
@Table(name = "fn_order_employee")
public class FnOrderEmployee extends BaseEntity<String> {

    //mã hóa đơn: mã E032021-1
    @Column(nullable = false, unique = true)
    private String code;

    private int year;

    private int month;

    private boolean locked;

    private Long idLocked;

    private LocalDateTime timeLock;

    @Column(columnDefinition = "TEXT")
    private String description;

    //true: cho nhân viên xem hóa đơn, false là không cho xem
    private boolean view;

    private Long idView;

    private LocalDateTime timeView;

    //true là nhân viên đã đọc
    private boolean employeeRead;

    @Column(nullable = false)
    private LocalDateTime timePayment = LocalDateTime.now();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

    @JsonManagedReference
    @OneToMany(mappedBy = "fnOrderEmployee", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderEmployeeHistory> orderEmployeeHistoryList;


}
