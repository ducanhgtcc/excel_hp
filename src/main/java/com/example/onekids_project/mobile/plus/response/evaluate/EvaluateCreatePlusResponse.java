package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.mobile.response.MobileFilePlus;
import com.example.onekids_project.mobile.response.MobileFileTeacher;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateCreatePlusResponse extends IdResponse {

    private String content;

    private List<MobileFilePlus> fileList;
}
