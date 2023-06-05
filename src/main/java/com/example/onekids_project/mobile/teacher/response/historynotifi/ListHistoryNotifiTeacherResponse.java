package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListHistoryNotifiTeacherResponse extends LastPageBase {

    private List<HistoryNotifiTeacherResponse> dataList;

}
