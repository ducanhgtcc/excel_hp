package com.example.onekids_project.mobile.teacher.response.notifyschool;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-10-22 10:54 AM
 *
 * @author nguyễn văn thụ
 */
@Setter
@Getter
public class NotifySchoolTeacherResponse {

    private String title;

    private String content;

    private String link;

    private LocalDateTime createdDate;

    private String createdBy;

    private List<AttachFileMobileResponse> fileList;
}
