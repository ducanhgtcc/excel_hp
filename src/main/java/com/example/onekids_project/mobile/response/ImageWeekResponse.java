package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageWeekResponse {

    private String weekName;

    private List<String> pictureList;
}
