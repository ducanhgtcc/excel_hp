package com.example.onekids_project.mobile.teacher.response.menuclass;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListMenuFileTeacherResponse extends LastPageBase {
    List<MenuFileTeacherResponse> dataList;
}
