package com.example.onekids_project.mobile.plus.response.feedbackplus;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListFeedbackPlusResponse extends LastPageBase {

    private List<FeedbackPlusResponse> dataList;

}
