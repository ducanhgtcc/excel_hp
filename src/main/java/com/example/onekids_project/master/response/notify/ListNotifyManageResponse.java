package com.example.onekids_project.master.response.notify;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-08-02 10:01 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListNotifyManageResponse extends TotalResponse {

    private List<NotifyManageResponse> dataList;
}
