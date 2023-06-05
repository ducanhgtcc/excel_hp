package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsPlusResponse extends IdResponse {

    private String fullName;

    private String content;

    private String avatar;

    private String createdDate;

    private boolean isFail;

    private String type;
}
