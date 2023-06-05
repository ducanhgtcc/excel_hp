package com.example.onekids_project.mobile.plus.response.notifyschool;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-10-22 11:06 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotifySchoolPlusResponse extends IdResponse {

    private String title;

    private String content;

    private String link;

    private boolean active;

    private LocalDateTime createdDate;

    private String createdBy;

    private List<AttachFileMobileResponse> fileList;
}
