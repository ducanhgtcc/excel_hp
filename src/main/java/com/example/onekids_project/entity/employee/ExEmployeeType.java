//package com.example.onekids_project.entity.employee;
//
//import com.example.onekids_project.entity.base.BaseEntityId;
//import com.example.onekids_project.entity.classes.MaClass;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "ex_employee_type")
//public class ExEmployeeType extends BaseEntityId<String> {
//
//    private Boolean isMaster;
//
//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_info_employee", nullable = false)
//    private InfoEmployeeSchool infoEmployeeSchool;
//
//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_account_type",nullable = false)
//    private AccountType accountType;
//}
