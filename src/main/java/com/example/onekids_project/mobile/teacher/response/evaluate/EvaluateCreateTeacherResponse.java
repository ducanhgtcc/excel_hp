package com.example.onekids_project.mobile.teacher.response.evaluate;

import com.example.onekids_project.mobile.response.MobileFileTeacher;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateCreateTeacherResponse extends IdRequest {
    private String content;

    private List<MobileFileTeacher> fileList;
}
