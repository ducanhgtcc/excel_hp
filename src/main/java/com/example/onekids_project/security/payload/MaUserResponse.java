package com.example.onekids_project.security.payload;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MaUserResponse extends IdResponse {
    private String username;

    private String passwordShow;

    private String fullName;

    private String appType;

    private boolean isActivated;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private boolean unlimitTime;

    private boolean demoStatus;

    private LocalDateTime fromDemoDate;

    private LocalDateTime toDemoDate;
}
