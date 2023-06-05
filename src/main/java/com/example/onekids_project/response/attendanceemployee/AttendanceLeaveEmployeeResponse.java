package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.request.attendanceemployee.AttendanceEmployeeConfigResponse;
import lombok.Data;

/**
 * date 2021-03-12 16:25
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceLeaveEmployeeResponse {

    private AttendanceEmployeeConfigResponse attendanceEmployeeConfig;

    private AttendanceInfoEmployeeResponse attendanceInfoEmployee;

    private AttendanceLeaveEmployeeDateResponse attendanceLeaveEmployee;

    private AttendanceTimeConfigResponse attendanceTimeConfig;

    private AttendanceArriveEmployeeDateResponse arriveEmployeeDate;

    //đã được điểm danh đến ở trạng thái đi làm 1 trong các buổi sáng, chiều hoặc tối
    private boolean isArrive;

    //đã được click vào 1 trong các ô điểm danh: đi làm, nghỉ có phép, nghỉ ko phép các buổi
    private boolean status;

    //nút để hiện thị button sửa hay không
    private boolean showEdit;

    //nút để xoay xoay khi lưu
    private boolean loadingSave;

}
