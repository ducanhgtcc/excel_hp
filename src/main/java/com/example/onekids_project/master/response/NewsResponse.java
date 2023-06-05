package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsResponse extends IdResponse {

    private String title;

    private String link;

    private boolean appPlus;

    private boolean appPlusUsed;

    private boolean appTeacher;

    private boolean appTeacherUsed;

    private boolean appParent;

    private boolean appParentUsed;

    private boolean appOneCame;

    private String urlAttachPicture;

    private LocalDateTime createdDate;

    private String createdBy;
}
