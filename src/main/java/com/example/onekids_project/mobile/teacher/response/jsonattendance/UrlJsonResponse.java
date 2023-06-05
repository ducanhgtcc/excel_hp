package com.example.onekids_project.mobile.teacher.response.jsonattendance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UrlJsonResponse {

    private String url;

    private LocalDateTime date;
}
