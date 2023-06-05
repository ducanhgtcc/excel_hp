package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.request.attendanceemployee.AttendanceEmployeeConfigResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-09 3:26 CH
 *
 * @author ADMIN
 */
@Getter
@Setter
public class AttendanceArriveEmployeeResponse {

    private AttendanceEmployeeConfigResponse attendanceConfig;

    private AttendanceArriveEmployeeDateResponse arriveEmployeeDate;

    private AttendanceInfoEmployeeResponse attendanceInfoEmployee;

    private AttendanceTimeConfigResponse attendanceTimeConfig;

    //trạng thái đã được điểm danh đến: đi làm, nghỉ có phép, nghỉ ko phép
    private boolean arrive;

    //nút để hiện thị button sửa hay không
    private boolean showEdit;

    //nút để xoay xoay khi lưu
    private boolean loadingSave;

}
