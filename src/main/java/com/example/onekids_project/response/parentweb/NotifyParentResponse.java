package com.example.onekids_project.response.parentweb;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.common.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class NotifyParentResponse extends IdResponse {
    private String title;

    private String content;

    private List<FileResponse> fileList = new ArrayList<>();

    private LocalDateTime createdDate;

    private boolean userUnread;
}
