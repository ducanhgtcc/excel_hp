package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BirthdaySampleActiveRequest extends IdRequest {
    private boolean smsSend;

    private boolean appSend;

    private boolean active;

}
