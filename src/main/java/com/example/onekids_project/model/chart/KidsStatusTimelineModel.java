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
public class KidsStatusTimelineModel {
    private int month;

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
