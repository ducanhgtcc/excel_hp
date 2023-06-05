package com.example.onekids_project.request.classmenu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabClassMenuDayWeekRequest {
    private String sessionDay;
    private String timeContent;
    private String contentClassMenu;
}
