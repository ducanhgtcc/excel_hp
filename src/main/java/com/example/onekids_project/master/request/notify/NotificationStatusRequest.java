package com.example.onekids_project.master.request.notify;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-08-02 1:57 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotificationStatusRequest extends IdRequest {
    private boolean status;
}
