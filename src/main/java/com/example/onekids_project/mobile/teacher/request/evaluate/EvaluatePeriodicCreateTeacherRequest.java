package com.example.onekids_project.mobile.teacher.request.evaluate;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EvaluatePeriodicCreateTeacherRequest {
    private Long id;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    @NotNull
    private Long idKid;

    @NotNull
    private String content;

    private List<MultipartFile> multipartFileList;

    private List<Long> idFileDeleteList;
}
