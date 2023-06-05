package com.example.onekids_project.mobile.teacher.response.feedback;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListFeedBackTeacherResponse extends LastPageBase {
    private List<FeedBackTeacherResponse> dataList;
}
