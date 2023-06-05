package com.example.onekids_project.mobile.teacher.request.attendacekids;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class AttendanceKidLeaveTeacherRequest {

    @NotNull
    List<Long> idLeave;

    private String content;

    private MultipartFile picture;

    private boolean deletePicture;

    private String time;

}
