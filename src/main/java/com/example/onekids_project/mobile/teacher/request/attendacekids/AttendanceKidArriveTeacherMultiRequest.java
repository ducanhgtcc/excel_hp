package com.example.onekids_project.mobile.teacher.request.attendacekids;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class AttendanceKidArriveTeacherMultiRequest {
    List<AttendanceOneKidArriveTeacherRequest> attendanceOneKidArriveList;
}
