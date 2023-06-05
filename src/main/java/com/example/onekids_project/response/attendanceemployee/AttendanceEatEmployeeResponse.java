package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.request.attendanceemployee.AttendanceEmployeeConfigResponse;
import lombok.Data;

/**
 * date 2021-03-15 13:29
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEatEmployeeResponse {

    private AttendanceEmployeeConfigResponse attendanceEmployeeConfig;

    private AttendanceInfoEmployeeResponse attendanceInfoEmployee;

    private AttendanceEatEmployeeDateResponse attendanceEatEmployee;

    private AttendanceArriveEmployeeDateResponse arriveEmployeeDate;

    //nút để xoay xoay khi lưu
    private boolean loadingSave;

}
