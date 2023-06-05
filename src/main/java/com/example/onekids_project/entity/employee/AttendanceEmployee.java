package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * date 2021-03-06 2:39 CH
 *
 * @author ADMIN
 */
@Getter
@Setter
@Entity
@Table(name = "ma_attendance_employee")
public class AttendanceEmployee extends BaseEntity<String> {

    @Column(nullable = false)
    private LocalDate date;

    private boolean morning;

    private boolean morningYes;

    private boolean morningNo;

    private boolean afternoon;

    private boolean afternoonYes;

    private boolean afternoonNo;

    private boolean evening;

    private boolean eveningYes;

    private boolean eveningNo;

    @Column(columnDefinition = "TEXT")
    private String arriveContent;

    @Column(length = 1000)
    private String arrivePicture;

    @Column(length = 1000)
    private String arriveLocalPicture;

    private LocalTime arriveTime;

    private int minuteArriveLate;

    private int minuteLeaveSoon;

    @Column(columnDefinition = "TEXT")
    private String leaveContent;

    @Column(length = 1000)
    private String leavePicture;

    @Column(length = 1000)
    private String leaveLocalPicture;

    private LocalTime leaveTime;

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

}
