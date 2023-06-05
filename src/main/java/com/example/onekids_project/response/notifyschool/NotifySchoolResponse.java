package com.example.onekids_project.response.notifyschool;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.common.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-10-21 9:50 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotifySchoolResponse extends IdResponse {

    private String title;

    private String content;

    private boolean active;

    private String link;

    private LocalDateTime createdDate;

    private String createdBy;

    private List<FileResponse> notifySchoolAttachFileList;
}
