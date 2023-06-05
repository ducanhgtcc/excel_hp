package com.example.onekids_project.response.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-26 8:51 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class EmployeeStatusTimelineResponse {

    private String name;

    //đang làm
    private int working;

    //tạm nghỉ
    private int retain;

    //nghỉ làm
    private int leave;

    //ra trường
    private int outSchool;
}
