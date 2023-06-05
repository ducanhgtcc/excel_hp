package com.example.onekids_project.mobile.teacher.request.attendacekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AiAttendanceKidLeaveTeacherRequest extends IdRequest {

    private String content;

    private MultipartFile picture;
}
