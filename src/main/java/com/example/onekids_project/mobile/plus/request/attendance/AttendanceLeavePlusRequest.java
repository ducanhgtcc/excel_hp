package com.example.onekids_project.mobile.plus.request.attendance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Data
public class AttendanceLeavePlusRequest {
    @NotNull
    private List<Long> idList;

    private String content;

    private MultipartFile picture;

    private boolean deletePicture;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;

}
