package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileAttachAppSendResponse extends IdResponse {
    private String name;
    private String url;
}
