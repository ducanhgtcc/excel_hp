package com.example.onekids_project.response.classmenu;

import com.example.onekids_project.response.schedule.FileAndPictureResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabDetailClassMenuAllClassResponse {
    private Long idClass;
    private String className;
    private String gradeName;
    private boolean isApprove;
    private String listCheckContentDay;
    private List<FileAndPictureMenuResponse> fileList;
    private String isMonday;
}
