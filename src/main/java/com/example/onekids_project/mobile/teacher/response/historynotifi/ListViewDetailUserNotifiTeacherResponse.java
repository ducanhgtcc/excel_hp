package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.mobile.teacher.response.message.MessageTeacherResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListViewDetailUserNotifiTeacherResponse  {

    private List<HistoryNotifiUserResponse> dataList;
}
