package com.example.onekids_project.mobile.teacher.response.jsonattendance;

import com.example.onekids_project.mobile.response.LastPageBase;
import com.example.onekids_project.mobile.teacher.response.historynotifi.HistoryNotifiTeacherResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListUrlJsonResponse  {

    @NotBlank
    private String url;

    private LocalDateTime date;
}
