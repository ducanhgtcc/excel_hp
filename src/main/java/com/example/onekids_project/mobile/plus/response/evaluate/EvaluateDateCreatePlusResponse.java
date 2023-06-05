package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.mobile.response.MobileFileTeacher;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

import java.util.List;

@Data
public class EvaluateDateCreatePlusResponse extends IdResponse {

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String healtContent;

    private String sanitaryContent;

    private String commonContent;

    private List<MobileFileTeacher> fileList;
}
