package com.example.onekids_project.response.changeQuery.chart;

import com.example.onekids_project.entity.kids.Kids;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-13 1:53 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@AllArgsConstructor
public class EvaluateKidsDateQueryResponse {

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String sanitaryContent;

    private String healtContent;

    private String commonContent;

    private Kids kids;
}
