package com.example.onekids_project.mobile.teacher.response.absentteacher;

import com.example.onekids_project.mobile.response.ReplyMobileObject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-05-26 9:20 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherDetailMobileResponse {

    private String fullName;

    private String avatar;

    private String content;

    private List<String> pictureList;

    private String dateAbsent;

    private List<AbsentTeacherDateMobileResponse> dateList;

    private boolean confirmStatus;

    private LocalDateTime createdDate;

    private List<ReplyMobileObject> replyList;
}
