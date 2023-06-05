package com.example.onekids_project.security.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class JwtCommonResponse {
    private LocalDate nowDate=LocalDate.now();

    private String quality;

    private String width;

    private List<JwtDataObject> weekList;

    private List<JwtDataObject> monthList;

}
