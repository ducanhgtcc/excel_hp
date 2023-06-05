package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ma_attendance_teacher")
public class AttendanceTeacher extends BaseEntityId<String> {

    @Column(nullable = false)
    private LocalDateTime attendanceDate;

    private boolean absentStatus;

    private Boolean absentLetterStatus;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

    @JsonManagedReference
    @OneToOne(mappedBy = "attendanceTeacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private AttendanceArriveTeacher attendanceArriveTeacher;

    @JsonManagedReference
    @OneToOne(mappedBy = "attendanceTeacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private AttendanceLeaveTeacher attendanceLeaveTeacher;

    @JsonManagedReference
    @OneToOne(mappedBy = "attendanceTeacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private AttendanceEatTeacher attendanceEatTeacher;
}
