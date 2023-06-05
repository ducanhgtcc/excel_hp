package com.example.onekids_project.mobile.plus.request.attendanceTeacher;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-06-09 10:59 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class LeaveManyRequest {
    List<AttendanceTeacherLeaveManyRequest> dataList;
}
