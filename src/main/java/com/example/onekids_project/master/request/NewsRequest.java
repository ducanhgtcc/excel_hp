package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewsRequest{

    private Long id;

//    @NotBlank
    private String title;

    @NotBlank
    private String link;

    private String[] appType;

    private boolean appPlus;

    private boolean appPlusUsed;

    private boolean appTeacher;

    private boolean appTeacherUsed;

    private boolean appParent;

    private boolean appParentUsed;

    private boolean appOneCame;

    private String urlAttachPicture;
}
