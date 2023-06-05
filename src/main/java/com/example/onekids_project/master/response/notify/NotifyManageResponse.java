package com.example.onekids_project.master.response.notify;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotifyManageResponse extends IdResponse {

    private String title;

    private String content;
    //Loại thông báo: NotificationConstant
    private String type;
    //plus-teacher-parent
    private String typeReceive;

    private String note;

    private boolean status= AppConstant.APP_FALSE;
    //-1, 0, 1-> tháng trước - tháng này - tháng sau
    private int month;

    List<NotifyManageDateResponse> notifyManageDateResponseList;
//
//    private Long idSchool;
//    //parent-1, teacher-2, plus-3
//    private int sortNumber;
}
