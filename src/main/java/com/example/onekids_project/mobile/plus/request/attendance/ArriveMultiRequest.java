package com.example.onekids_project.mobile.plus.request.attendance;

import lombok.Data;

import java.util.List;

@Data
public class ArriveMultiRequest {
    private List<AttendanceArriveMultiPlusRequest> arriveMultiRequest;
}
