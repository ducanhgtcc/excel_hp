package com.example.onekids_project.master.response.notify;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListNotifyAdminResponse extends TotalResponse {
    private List<NotifyAdminResponse> dataList;
}
