package com.example.onekids_project.response.feedback;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListFeedBackResponse  extends TotalResponse {
    List<FeedBackResponse> feedBackResponses;
}
