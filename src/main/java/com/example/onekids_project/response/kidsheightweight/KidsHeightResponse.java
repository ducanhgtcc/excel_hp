package com.example.onekids_project.response.kidsheightweight;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class KidsHeightResponse extends IdResponse {

    private Double height;

    private LocalDate timeHeight;

    private String appType;

    private boolean delActive;

    private String createdBy;
}
