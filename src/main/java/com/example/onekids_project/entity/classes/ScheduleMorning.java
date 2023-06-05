package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "schedule_morning")
public class ScheduleMorning extends BaseEntity<String> {

    private String time;

    @Column(columnDefinition = "TEXT")
    private String content = "";

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class_schedule", nullable = false)
    private ClassSchedule classSchedule;

}
