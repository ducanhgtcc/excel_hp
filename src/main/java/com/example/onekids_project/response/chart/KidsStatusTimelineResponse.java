package com.example.onekids_project.response.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-18 3:35 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class KidsStatusTimelineResponse {

    private String name;

    //đang học
    private int studying;

    //chờ học
    private int studyWait;

    //bảo lưu
    private int reserve;

    //nghỉ học
    private int leaveSchool;

    //ra trường
    private int outSchool;
}
