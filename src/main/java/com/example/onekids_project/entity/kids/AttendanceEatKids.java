package com.example.onekids_project.entity.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntityNoAuditing;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "attendance_eat_kids")
public class AttendanceEatKids extends BaseEntityNoAuditing<String> {

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean breakfast;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean secondBreakfast;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean lunch;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean afternoon;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean secondAfternoon;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean dinner;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_attendance", nullable = false)
    private AttendanceKids attendanceKids;
}
