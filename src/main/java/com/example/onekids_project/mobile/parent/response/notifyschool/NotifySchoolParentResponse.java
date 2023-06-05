package com.example.onekids_project.mobile.parent.response.notifyschool;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-10-22 10:21 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotifySchoolParentResponse extends IdResponse {

    private String title;

    private String content;

    private String link;

    private LocalDateTime createdDate;

    private String createdBy;

    private List<AttachFileMobileResponse> fileList;

}
