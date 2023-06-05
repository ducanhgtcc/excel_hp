package com.example.onekids_project.response.parentweb;

import com.example.onekids_project.mobile.parent.response.home.MobileNotifiParentResponse;
import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class ListNotifyParentResponse extends TotalResponse {
    private List<NotifyParentResponse> dataList;
}
