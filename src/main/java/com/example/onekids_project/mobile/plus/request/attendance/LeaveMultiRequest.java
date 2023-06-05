package com.example.onekids_project.mobile.plus.request.attendance;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LeaveMultiRequest {
    List<AttendanceLeaveMultiPlusRequest> leaveMultiRequest;
}
