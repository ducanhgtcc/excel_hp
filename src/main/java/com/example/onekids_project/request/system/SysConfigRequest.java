package com.example.onekids_project.request.system;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.time.LocalTime;

@Getter
@Setter
public class SysConfigRequest extends IdRequest {

    private boolean appSendParent;

    private boolean appSendTeacher;

    private int numberRemindParent;

    private int jumpTimeParent;

    private int numberRemindTeacher;

    private int jumpTimeTeacher;

    private int numberRemindPlus;

    private int jumpTimePlus;

    private LocalTime timeFrom;

    private LocalTime timeTo;

    private int numberOutSchool;

    private int numberNotify;

    private int jumpNumberOut;

    private LocalTime timeSendCelebrate;

    private boolean showTitleSms;

    private String titleContentSms;

    private int widthAlbum;

    private int widthOther;

    private int mobileSizePage;

    private boolean appplusSmsSend;

    private boolean appteacherSmsSend;

    private boolean appparentSmsSend;

    private boolean userReceiveSms;

    private String qualityPicture;

    private String widthPicture;

    private boolean deleteAccountStatus;

    @Min(1)
    private int deleteAccountDate;

}
