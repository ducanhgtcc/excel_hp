package com.example.onekids_project.entity.sample;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityId;
import com.example.onekids_project.entity.school.School;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "attendance_sample")
public class AttendanceSample extends BaseEntity<String> {

//    AttendanceConstant: arrive, leave
    @Column(nullable = false, length = 255)
    private String attendanceType;

    @Column(nullable = false, length = 500)
    private String attendanceContent;

    @Column(nullable = false)
    private Long idSchool;
}
