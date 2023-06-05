package com.example.onekids_project.master.response.notify;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * date 2021-08-12 2:23 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class InternalNotificationPlusResponse extends IdResponse {

    private LocalDateTime createdDate;

    private String title;

    private String content;

    private String fullName;
}
