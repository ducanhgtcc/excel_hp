package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.mobile.response.LastPageBase;
import com.example.onekids_project.mobile.teacher.response.historynotifi.HistoryNotifiUserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListViewDetailUserNotifiPlusResponse extends LastPageBase {

    private boolean check;

    private List<DetailUserHistoryNotifiAppResponse> responseList;
}
