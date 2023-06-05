package com.example.onekids_project.mobile.parent.response.home;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MobileNotifiParentResponse extends IdResponse {
    private String title;

    private String content;

    private String avatar;

    private int pictureNumber;

    private int fileNumber;

    private LocalDateTime createdDate;

    private boolean seen;
}
