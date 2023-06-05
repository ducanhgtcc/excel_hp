package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.mobile.response.ListFileNotifi;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailUserHistoryNotifiAppResponse extends IdResponse {

    private String fullName;

    private String avatar;

    private String status;

}
