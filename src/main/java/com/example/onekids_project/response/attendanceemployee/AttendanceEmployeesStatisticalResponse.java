package com.example.onekids_project.response.attendanceemployee;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-18 17:11
 *
 * @author Phạm Ngọc Thắng
 */

@Getter
@Setter
public class AttendanceEmployeesStatisticalResponse {

    //đi làm cả ngày
    private int allDay;

    //nghỉ làm có phép cả ngày
    private int allDayYes;

    //nghỉ làm ko phép cả ngày
    private int allDayNo;

    //đi làm sáng
    private int morning;

    //nghỉ có phép sáng
    private int morningYes;

    //nghỉ ko phép sáng
    private int morningNo;

    //đi làm chiều
    private int afternoon;

    //nghỉ có phép chiều
    private int afternoonYes;

    //nghỉ không phép chiều
    private int afternoonNo;

    //đi làm tối
    private int evening;

    //nghỉ có phép tối
    private int eveningYes;

    //nghỉ ko phép tối
    private int eveningNo;

    private int eatAllDay;

    //bữa sáng
    private int eatMorning;

    //bữa trưa
    private int eatNoon;

    //bữa chiều
    private int eatAfternoon;

    //bữa tối
    private int eatEvening;

    //số phút đến muộn
    private int minutesArriveLate;

    //số phút về sớm
    private int minutesLeaveSoon;

    //tính đi làm cả ngày hoặc nửa buổi
    private float goSchoolTime;

    private float absentTime;

    private float absentDateToDateNo26;
    private float absentDateToDateYesNo26;
    private float absentDateToDateYes78;
    private float absentDateToDateYesNo78;
}
