package com.example.onekids_project.mobile.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NewsMobileResponse {
    private LocalDate date;

    private String title;

    private String link;

    private String picture;
}
