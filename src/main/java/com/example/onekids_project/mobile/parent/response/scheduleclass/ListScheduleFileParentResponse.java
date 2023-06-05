package com.example.onekids_project.mobile.parent.response.scheduleclass;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListScheduleFileParentResponse extends LastPageBase {
    private List<ScheduleFileParentResponse> dataList;
}
