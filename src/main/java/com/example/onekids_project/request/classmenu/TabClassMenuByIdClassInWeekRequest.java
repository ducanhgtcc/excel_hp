package com.example.onekids_project.request.classmenu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabClassMenuByIdClassInWeekRequest {
    private Long idClass;
    private String timeClassMenu;
    private Long idClassMenu;
    private List<TabClassMenuDayWeekRequest> tabClassMenuDayClassList;
}
