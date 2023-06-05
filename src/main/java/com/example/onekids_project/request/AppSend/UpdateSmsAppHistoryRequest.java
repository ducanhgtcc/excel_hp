package com.example.onekids_project.request.AppSend;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSmsAppHistoryRequest extends IdRequest {

    private boolean sendDel;
}
