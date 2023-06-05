package com.example.onekids_project.mobile.plus.response.absent;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAbsentPlusResponse extends LastPageBase {

    private List<AbsentPlusResponse> dataList;

}
