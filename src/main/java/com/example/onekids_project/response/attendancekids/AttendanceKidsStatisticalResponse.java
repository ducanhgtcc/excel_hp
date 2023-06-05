package com.example.onekids_project.response.attendancekids;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-11 15:36
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class AttendanceKidsStatisticalResponse {
    //đi học cả ngày
    private int allDay;

    //nghỉ học có phép cả ngày
    private int allDayYes;

    //nghỉ học ko phép cả ngày
    private int allDayNo;

    //đi học sáng
    private int morning;

    //nghỉ có phép sáng
    private int morningYes;

    //nghỉ ko phép sáng
    private int morningNo;

    //đi học chiều
    private int afternoon;

    //nghỉ có phép chiều
    private int afternoonYes;

    //nghỉ không phép chiều
    private int afternoonNo;

    //đi học tối
    private int evening;

    //nghỉ có phép tối
    private int eveningYes;

    //nghỉ ko phép tối
    private int eveningNo;

    private int eatAllDay;

    //bữa sáng
    private int eatMorning;

    //bữa phụ sáng
    private int eatMorningSecond;

    //bữa trưa
    private int eatNoon;

    //bữa chiều
    private int eatAfternoon;

    //bữa phụ chiều
    private int eatAfternoonSecond;

    //bữa tối
    private int eatEvening;

    //số phút muộn
    private int minutesLate;

}
