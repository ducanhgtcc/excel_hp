package com.example.onekids_project.mobile.plus.request.attendance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AiAttendanceKidLeavePlusRequest extends IdRequest {

    private Long idClass;

    // id Kid
    private String content;

    private MultipartFile picture;
}
