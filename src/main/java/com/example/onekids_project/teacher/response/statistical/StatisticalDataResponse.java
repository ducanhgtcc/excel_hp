package com.example.onekids_project.teacher.response.statistical;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-04-19 10:29
 *
 * @author lavanviet
 */
@Getter
@Setter
public class StatisticalDataResponse {
    private List<StatisticalAttendanceArrive> statisticalAttendanceArriveGradeList;

    private List<StatisticalAttendanceArrive> statisticalAttendanceArriveClassList;

    private List<StatisticalAttendanceEat> statisticalAttendanceEatGradeList;

    private List<StatisticalAttendanceEat> statisticalAttendanceEatClassList;

}
