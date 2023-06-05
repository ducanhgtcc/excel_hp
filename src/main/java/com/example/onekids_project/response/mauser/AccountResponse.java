package com.example.onekids_project.response.mauser;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.device.DeviceLoginResponse;
import com.example.onekids_project.response.device.DeviceOneCamResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AccountResponse extends IdResponse {
    private String fullName;

    private String username;

    private String extendUsername;

    private String passwordShow;

    private String phone;

    private boolean activated;

    private LocalDate fromDate;

    private LocalDate toDate;

    private boolean unlimitTime;

    private boolean demoStatus;

    private LocalDate fromDemoDate;

    private LocalDate toDemoDate;

    private LocalDateTime createdDate;

    private LocalDate startDateDelete;

    private String typeDelete;

    private LocalDateTime timeDelete;

    private LocalDateTime timeDeleteComplete;

    private boolean trialStatus;

    private List<DeviceLoginResponse> deviceLoginList;

    private List<DeviceOneCamResponse> deviceCamList;
}
