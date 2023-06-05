package com.example.onekids_project.mobile.plus.response.schedule;

import com.example.onekids_project.mobile.response.ScheduleDateResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleDatePlusResponse {
    
    private String title;

    private List<ScheduleDateResponse> morningList;

    private List<ScheduleDateResponse> afternoonList;

    private List<ScheduleDateResponse> eveningList;
}
