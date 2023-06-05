package com.example.onekids_project.request.feedback;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateParentFeedbackRequest extends IdRequest {

    private String schoolReply;

}
