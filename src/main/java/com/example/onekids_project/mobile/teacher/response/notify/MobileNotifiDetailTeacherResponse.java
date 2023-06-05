package com.example.onekids_project.mobile.teacher.response.notify;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class MobileNotifiDetailTeacherResponse {

    private String title;

    private String content;

    private LocalDateTime createdDate;

    private List<String> listImage;

    private List<AttachFileMobileResponse> fileList;
}
