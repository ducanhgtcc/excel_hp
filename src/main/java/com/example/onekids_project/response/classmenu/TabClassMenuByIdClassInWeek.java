package com.example.onekids_project.response.classmenu;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;

@Getter
@Setter
public class TabClassMenuByIdClassInWeek {
    private Long idClass;
    private Long idClassMenu;
    private String timeClassMenu;
    private List<TabClassMenuDayWeekResponse> tabClassMenuDayClassList;
}
