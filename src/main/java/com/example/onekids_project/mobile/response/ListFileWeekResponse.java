package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListFileWeekResponse extends LastPageBase {

    List<FileWeekResponse> dataList;
}
