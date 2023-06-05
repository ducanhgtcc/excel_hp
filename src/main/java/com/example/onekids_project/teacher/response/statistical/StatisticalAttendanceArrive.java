package com.example.onekids_project.teacher.response.statistical;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-04-19 10:31
 *
 * @author lavanviet
 */
@Getter
@Setter
public class StatisticalAttendanceArrive extends IdResponse {
    private String name;

    private int studyingNumber;

    private int goSchoolNumber;

    private int absentNumber;

    private int noAttendance;
}
