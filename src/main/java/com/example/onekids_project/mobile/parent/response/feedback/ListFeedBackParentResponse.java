package com.example.onekids_project.mobile.parent.response.feedback;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListFeedBackParentResponse extends LastPageBase {
    private List<FeedBackParentResponse> feedbackList;
}
