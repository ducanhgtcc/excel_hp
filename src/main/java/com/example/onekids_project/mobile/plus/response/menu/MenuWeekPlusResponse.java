package com.example.onekids_project.mobile.plus.response.menu;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MenuWeekPlusResponse {
    
    private LocalDate date;

    private List<MenuDatePlusResponse> menuDateList;
}
