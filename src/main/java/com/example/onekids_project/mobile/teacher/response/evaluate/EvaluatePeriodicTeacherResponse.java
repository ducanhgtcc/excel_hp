package com.example.onekids_project.mobile.teacher.response.evaluate;

import com.example.onekids_project.mobile.response.MobileFileTeacher;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EvaluatePeriodicTeacherResponse extends IdResponse {
    private LocalDateTime dateTime = LocalDateTime.now();

    private Long idKid;

    private boolean status;

    private String kidName;

    private String avatar;

    private boolean approved;

    private String content;

    private List<MobileFileTeacher> fileList;
}
