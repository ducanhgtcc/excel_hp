package com.example.onekids_project.mobile.parent.response.menuclass;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListMenuFileParentResponse extends LastPageBase {
    List<MenuFileParentResponse> dataList;
}
