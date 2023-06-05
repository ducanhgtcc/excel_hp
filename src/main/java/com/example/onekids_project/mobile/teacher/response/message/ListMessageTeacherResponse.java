package com.example.onekids_project.mobile.teacher.response.message;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMessageTeacherResponse extends LastPageBase {

    private List<MessageTeacherResponse> dataList;

}
