package com.example.onekids_project.mobile.plus.response.menu;

import com.example.onekids_project.mobile.response.MenuStatusDay;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuClassWeekResponse extends IdResponse {

    String nameClass;

    boolean isMenu;

    List<MenuStatusDay> statusDayList;
}
