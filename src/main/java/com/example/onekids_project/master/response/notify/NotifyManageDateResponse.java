package com.example.onekids_project.master.response.notify;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class NotifyManageDateResponse extends IdResponse {

    private LocalTime time;

    private Integer minute;

    private Integer hour;

    private Integer day;

    private Integer month;

    private boolean status;

    private Double percent;
}
