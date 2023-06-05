package com.example.onekids_project.master.request.notify;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * date 2021-08-09 6:11 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotifyManageDateRequest extends IdRequest {

    private Long idNotification;

    private LocalTime time;

    private Integer day;

    private Integer month;

    private boolean status;

    private Double percent;
}
