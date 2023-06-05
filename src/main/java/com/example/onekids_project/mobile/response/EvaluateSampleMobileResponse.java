package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateSampleMobileResponse {
    private List<String> learnList;

    private List<String> eatList;

    private List<String> sleepList;

    private List<String> sanitaryList;

    private List<String> healtList;

    private List<String> commonList;
}
