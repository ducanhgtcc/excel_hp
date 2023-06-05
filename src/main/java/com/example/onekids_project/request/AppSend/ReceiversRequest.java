package com.example.onekids_project.request.AppSend;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiversRequest extends IdResponse {
    private boolean isApproved;
}
