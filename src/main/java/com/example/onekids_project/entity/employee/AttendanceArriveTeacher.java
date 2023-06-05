package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attendance_arrive_teacher")
public class AttendanceArriveTeacher extends BaseEntity<String> {

    private Boolean morning;

    private Boolean afternoon;

    private Boolean evening;

    @Column(length = 1000)
    private String arriveContent;

    private String arriveUrlPicture;

    @Column(nullable = false)
    private LocalDateTime attendanceTime;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_attendance_teacher", nullable = false)
    private AttendanceTeacher attendanceTeacher;
}
