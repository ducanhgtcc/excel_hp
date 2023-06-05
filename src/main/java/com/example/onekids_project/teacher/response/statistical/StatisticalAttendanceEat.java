package com.example.onekids_project.teacher.response.statistical;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * date 2021-04-19 10:32
 *
 * @author lavanviet
 */
@Getter
@Setter
public class StatisticalAttendanceEat extends IdResponse {
    private String name;

    private int allDay;

    private int breakfast;

    private int secondBreakfast;

    private int lunch;

    private int afternoon;

    private int secondAfternoon;

    private int dinner;

}
