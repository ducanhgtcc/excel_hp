package com.example.onekids_project.response.classmenu;

import com.example.onekids_project.response.schedule.FileAndPictureResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabClassMenuViewDetail {
    private Long idClass;
    private String isMonday;
    private String weeknumber;
    private String timeApplyWeek;
    private boolean isApprove;
    private String listCheckContentday;
    private String listFileOrPicture;
    private List<FileAndPictureMenuResponse> fileList;
}
