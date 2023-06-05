package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attendance_eat_teacher")
public class AttendanceEatTeacher extends BaseEntity<String> {

    private Boolean breakfast;

    private Boolean lunch;

    private Boolean dinner;

    @Column(nullable = false)
    private LocalDateTime attendanceTime;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_attendance_teacher", nullable = false)
    private AttendanceTeacher attendanceTeacher;
}
