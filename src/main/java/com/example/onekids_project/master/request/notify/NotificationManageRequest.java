package com.example.onekids_project.master.request.notify;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-08-02 10:45 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotificationManageRequest extends IdRequest {

    private String title;

    private String content;

    private String note;

    private boolean status= AppConstant.APP_TRUE;
    //-1, 0, 1-> tháng trước - tháng này - tháng sau
    private int month;
}
