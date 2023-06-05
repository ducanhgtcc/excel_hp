package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.mobile.plus.response.MessagePlusResponse;
import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListNotifiSysResponse extends LastPageBase {

    private List<NotifiSysResponse> dataList;

}
