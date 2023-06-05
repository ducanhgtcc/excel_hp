package com.example.onekids_project.response.school;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListAppIconPlusResponse {

    private List<AppIconPlusResponse> appIconPlusResponseList;

    private List<AppIconPlusResponse> appIconPlusResponseList1;

    private List<AppIconPlusResponse> appIconPlusResponseList2;

    public ListAppIconPlusResponse() {
        this.appIconPlusResponseList = new ArrayList<>();
        this.appIconPlusResponseList1 = new ArrayList<>();
        this.appIconPlusResponseList2 = new ArrayList<>();
    }
}
