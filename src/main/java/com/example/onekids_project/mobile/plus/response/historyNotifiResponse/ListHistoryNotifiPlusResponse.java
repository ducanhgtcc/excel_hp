package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListHistoryNotifiPlusResponse extends LastPageBase {

    private List<HistoryNotifiPlusResponse> dataList;

}
