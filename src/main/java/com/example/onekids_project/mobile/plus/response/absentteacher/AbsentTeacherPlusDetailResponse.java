package com.example.onekids_project.mobile.plus.response.absentteacher;

import com.example.onekids_project.mobile.parent.response.absentletter.AbsentDateDataResponse;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.teacher.response.absentteacher.AbsentTeacherDateMobileResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-05-31 10:18 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherPlusDetailResponse {

    private String fullName;

    private boolean confirmStatus;

    private String avatar;

    private String content;

    private boolean checkPlusReply = false;

    private List<String> pictureList;

    private String dateAbsent;

    private String createdDate;

    private List<AbsentTeacherDateMobileResponse> absentDateList;

    private List<ReplyMobilePlusObject> replyList;

}
