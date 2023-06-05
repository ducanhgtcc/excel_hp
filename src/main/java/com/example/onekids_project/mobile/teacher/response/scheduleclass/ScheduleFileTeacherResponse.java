package com.example.onekids_project.mobile.teacher.response.scheduleclass;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class ScheduleFileTeacherResponse {

    private LocalDate date;

    private String week;

    private List<AttachFileMobileResponse> fileList;
}
