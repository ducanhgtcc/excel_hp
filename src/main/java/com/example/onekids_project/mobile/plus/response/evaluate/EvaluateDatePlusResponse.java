package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.mobile.response.MobileFileTeacher;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EvaluateDatePlusResponse extends IdResponse {

    private LocalDateTime dateTime = LocalDateTime.now();

    private boolean status;

    private String kidName;

    private String avatar;

    private boolean approved;

    private boolean goSchoolStatus;

    private String attendanceStatus;

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String healtContent;

    private String sanitaryContent;

    private String commonContent;

    private List<MobileFileTeacher> fileList;
}
