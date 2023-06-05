package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsExtraResponse extends IdResponse {

    private String title;

    private String link;

    private boolean appPlus;

    private boolean appPlusUsed;

    private boolean appTeacher;

    private boolean appTeacherUsed;

    private boolean appParent;

    private boolean appParentUsed;

}
