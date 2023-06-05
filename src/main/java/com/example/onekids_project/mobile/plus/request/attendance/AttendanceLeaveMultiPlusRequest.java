package com.example.onekids_project.mobile.plus.request.attendance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class AttendanceLeaveMultiPlusRequest {
    @NotNull
    private Long id;

    private String content;

    private MultipartFile picture;

    private boolean deletePicture;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;

}
