package com.example.onekids_project.mobile.teacher.response.evaluate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticalOfMonthTeacherResponse {
    private Long idKid;

    private String kidName;

    private String avatar;

    private String kidsStatus;

    private int dateNumber;

    private int weekNumber;

    private int monthNumber;

    private int periodicNumber;

    private boolean parentReplyUnread;
}
