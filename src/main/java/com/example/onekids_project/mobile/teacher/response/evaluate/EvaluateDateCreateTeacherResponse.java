package com.example.onekids_project.mobile.teacher.response.evaluate;

import com.example.onekids_project.mobile.response.MobileFileTeacher;
import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateDateCreateTeacherResponse extends IdResponse {
    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String healtContent;

    private String sanitaryContent;

    private String commonContent;

    private List<MobileFileTeacher> fileList;
}
