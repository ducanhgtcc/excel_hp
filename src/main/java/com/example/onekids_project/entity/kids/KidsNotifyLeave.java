package com.example.onekids_project.entity.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "kids_notify_leave")
public class KidsNotifyLeave extends BaseEntity<String > {

    @Column(nullable = false)
    private Long idSchoolOld;

    @Column(nullable = false)
    private LocalDate dateLeaveSchooll;

    @Column(nullable = false,columnDefinition = "int default 10")
    private  int numberOutSchool=10;

    @Column(nullable = false,columnDefinition = "int default 3")
    private int jumpNumberOut=3;

    @Column(nullable = false)
    private String listIdUserPlus;

    @Column(nullable = false,columnDefinition = "bit default true")
    private Boolean isRemind= AppConstant.APP_TRUE;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids")
    private Kids kids;
}
