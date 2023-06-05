package com.example.onekids_project.request.attendancekids;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttendanceLeaveUpdateContentRequest {
    private String leaveContent;

    private MultipartFile multipartFile;
}
