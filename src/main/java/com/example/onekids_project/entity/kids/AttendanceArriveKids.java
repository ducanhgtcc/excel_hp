package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntityNoAuditing;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "attendance_arrive_kids")
public class AttendanceArriveKids extends BaseEntityNoAuditing<String> {

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean morning;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean morningYes;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean morningNo;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean afternoon;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean afternoonYes;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean afternoonNo;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean evening;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean eveningYes;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean eveningNo;

    @Column(length = 3000)
    private String arriveContent = "";

    @Column(length = 500)
    private String arriveUrlPicture;

    @Column(length = 500)
    private String arriveUrlPictureLocal;

    private LocalTime timeArriveKid;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_attendance", nullable = false)
    private AttendanceKids attendanceKids;
}
