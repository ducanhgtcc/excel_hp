package com.example.onekids_project.response.school;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListAppIconResponse {
    private List<AppIconResponse> appIconResponseList;

    private List<AppIconResponse> appIconResponseList1;

    private List<AppIconResponse> appIconResponseList2;

    public ListAppIconResponse() {
        this.appIconResponseList = new ArrayList<>();
        this.appIconResponseList1 = new ArrayList<>();
        this.appIconResponseList2 = new ArrayList<>();
    }
}
