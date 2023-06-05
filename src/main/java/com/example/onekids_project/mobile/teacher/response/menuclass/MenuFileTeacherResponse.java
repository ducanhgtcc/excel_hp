package com.example.onekids_project.mobile.teacher.response.menuclass;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class MenuFileTeacherResponse {
    private LocalDate date;

    private String week;

    private List<AttachFileMobileResponse> fileList;
}
