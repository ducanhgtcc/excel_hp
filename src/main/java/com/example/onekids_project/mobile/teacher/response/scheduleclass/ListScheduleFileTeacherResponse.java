package com.example.onekids_project.mobile.teacher.response.scheduleclass;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListScheduleFileTeacherResponse extends LastPageBase {
    private List<ScheduleFileTeacherResponse> dataList;
}
