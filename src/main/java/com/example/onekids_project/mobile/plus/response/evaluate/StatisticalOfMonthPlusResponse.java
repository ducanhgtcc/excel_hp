package com.example.onekids_project.mobile.plus.response.evaluate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticalOfMonthPlusResponse {
    private Long idKid;

    private String kidName;

    private String kidsStatus;

    private String avatar;

    private int dateNumber;

    private int weekNumber;

    private int monthNumber;

    private int periodicNumber;

    private boolean parentReplyUnread;

}
