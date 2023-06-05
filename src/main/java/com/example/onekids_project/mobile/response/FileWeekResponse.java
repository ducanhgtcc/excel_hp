package com.example.onekids_project.mobile.response;

import com.example.onekids_project.response.common.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FileWeekResponse {

    private LocalDate date;

    private String weekName;

    private List<FileResponse> fileList;
}
