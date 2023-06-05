package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-05-18 8:55 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class EvaluateDateKidExcelResponse extends IdResponse {

    private LocalDate date;

    private boolean approved;

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String sanitaryContent;

    private String healtContent;

    private String commonContent;

    private KidOtherResponse kids;
}
