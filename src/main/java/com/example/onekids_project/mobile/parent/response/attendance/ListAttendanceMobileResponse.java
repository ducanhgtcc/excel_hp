package com.example.onekids_project.mobile.parent.response.attendance;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAttendanceMobileResponse extends LastPageBase {
    private List<AttendanceMobileResponse> dataList;
}
