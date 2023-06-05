package com.example.onekids_project.response.school;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListAppIconTeacherResponse {
    private List<AppIconTeacherResponse> appIconTeacherResponseList;

    private List<AppIconTeacherResponse> appIconTeacherResponseList1;

    private List<AppIconTeacherResponse> appIconTeacherResponseList2;

    public ListAppIconTeacherResponse() {
        this.appIconTeacherResponseList = new ArrayList<>();
        this.appIconTeacherResponseList1 = new ArrayList<>();
        this.appIconTeacherResponseList2 = new ArrayList<>();
    }
}
