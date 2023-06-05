package com.example.onekids_project.master.response.feedback;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListFeedbackAdminResponse extends TotalResponse {
    private List<FeedbackAdminResponse> dataList;
}
