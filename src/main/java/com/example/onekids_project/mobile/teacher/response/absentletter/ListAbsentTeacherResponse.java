package com.example.onekids_project.mobile.teacher.response.absentletter;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAbsentTeacherResponse extends LastPageBase {

    private List<AbsentTeacherResponse> dataList;

}
