package com.example.onekids_project.response.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabDetailScheduleAllClassResponse {
    private Long idClass;
    private String className;
    private String gradeName;
    private boolean isApprove;
    private String listCheckContentDay;
    private List<FileAndPictureResponse> fileList;
    private String isMonday;
}
