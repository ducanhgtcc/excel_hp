package com.example.onekids_project.response.classmenu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabAllClassMenuInWeekResponse {
    private Long idClass;
    private String className;
    private Long idSchool;
    private boolean morningSaturday;
    private boolean afternoonSaturday;
    private boolean eveningSaturday;
    private boolean sunday;
    private Boolean approve;
    List<TabClassMenuResponse> tabClassMenuList;
}
