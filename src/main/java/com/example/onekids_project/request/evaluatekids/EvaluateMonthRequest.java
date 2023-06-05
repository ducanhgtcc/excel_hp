package com.example.onekids_project.request.evaluatekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateMonthRequest extends IdRequest {
    private boolean isApproved;

    private String content;

    private String urlFileList;
}
