package com.example.onekids_project.entity.onecam;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table
public class OneCamSetting extends BaseEntity<String> {
    private boolean arriveViewStatus;

    @Column(nullable = false)
    private String arriveViewText;


    private boolean leaveNoViewStatus;

    @Column(nullable = false)
    private String leaveNoViewText;


    private boolean viewLimitStatus;

    @Column(nullable = false)
    private String viewLimitText;

    private int viewLimitNumber;


    private boolean timeViewStatus;

    @Column(nullable = false)
    private String timeViewText;

    private LocalTime startTime1;

    private LocalTime endTime1;

    private LocalTime startTime2;

    private LocalTime endTime2;

    private LocalTime startTime3;

    private LocalTime endTime3;

    private LocalTime startTime4;

    private LocalTime endTime4;

    private LocalTime startTime5;

    private LocalTime endTime5;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;

}
