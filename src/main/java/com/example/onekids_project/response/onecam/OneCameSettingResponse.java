package com.example.onekids_project.response.onecam;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author lavanviet
 */
@Data
public class OneCameSettingResponse extends IdResponse {
    private String className;


    private boolean arriveViewStatus;

    private String arriveViewText;


    private boolean leaveNoViewStatus;

    private String leaveNoViewText;


    private boolean viewLimitStatus;

    private String viewLimitText;

    private int viewLimitNumber;


    private boolean timeViewStatus;

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

}
