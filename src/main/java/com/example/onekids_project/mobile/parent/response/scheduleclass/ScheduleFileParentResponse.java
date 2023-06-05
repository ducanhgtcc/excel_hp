package com.example.onekids_project.mobile.parent.response.scheduleclass;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ScheduleFileParentResponse {
    private LocalDate date;

    private String week;

    private List<AttachFileMobileResponse> fileList;
}
