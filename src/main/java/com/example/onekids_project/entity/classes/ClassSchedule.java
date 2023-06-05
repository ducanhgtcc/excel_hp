package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_class_schedule")
public class ClassSchedule extends BaseEntity<String> {

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private LocalDate scheduleDate;

    private String scheduleTitle;

    @Column(nullable = false)
    private boolean isApproved;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;

    @JsonManagedReference
    @OneToMany(mappedBy = "classSchedule", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ScheduleMorning> scheduleMorningList;

    @JsonManagedReference
    @OneToMany(mappedBy = "classSchedule", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ScheduleAfternoon> scheduleAfternoonList;

    @JsonManagedReference
    @OneToMany(mappedBy = "classSchedule", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ScheduleEvening> scheduleEveningList;

}
