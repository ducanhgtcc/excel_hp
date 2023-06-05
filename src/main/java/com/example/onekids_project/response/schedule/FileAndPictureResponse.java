package com.example.onekids_project.response.schedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileAndPictureResponse {
    private String name;
    private String url;
    private Long idScheduleFile;
    private Long idUrlScheduleFile;
    private String extensionUrlFile;
}
