package com.example.onekids_project.model.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-18 09:29
 *
 * @author lavanviet
 */
@Getter
@Setter
public class EmployeeStatusTimelineModel {
    private int month;

    //đang làm
    private int working;

    //tạm nghỉ
    private int retain;

    //nghỉ làm
    private int leave;

    //ra trường
    private int outSchool;

}
