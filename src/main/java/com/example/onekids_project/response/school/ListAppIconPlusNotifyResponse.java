package com.example.onekids_project.response.school;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListAppIconPlusNotifyResponse {

    private List<AppIconPlusNotifyResponse> appIconPlusNotifyResponseList;

    private List<AppIconPlusNotifyResponse> appIconPlusNotifyResponseList1;

    private List<AppIconPlusNotifyResponse> appIconPlusNotifyResponseList2;

    public ListAppIconPlusNotifyResponse() {
        this.appIconPlusNotifyResponseList = new ArrayList<>();
        this.appIconPlusNotifyResponseList1 = new ArrayList<>();
        this.appIconPlusNotifyResponseList2 = new ArrayList<>();
    }
}
