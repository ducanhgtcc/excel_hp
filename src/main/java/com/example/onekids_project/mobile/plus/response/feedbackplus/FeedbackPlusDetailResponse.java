package com.example.onekids_project.mobile.plus.response.feedbackplus;

import com.example.onekids_project.mobile.plus.response.ReplyPlusObject;
import com.example.onekids_project.mobile.plus.response.department.EmployeeAccoutTypeResponse;
import com.example.onekids_project.mobile.plus.response.video.ReplyPlusNewObject;
import com.example.onekids_project.mobile.response.ReplyTeacherObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedbackPlusDetailResponse {

    private String title;

    private String createdName;

    private String avatar;

    private String content;

    private boolean checkSchoolReply;

    private boolean schoolMoidifystatus;


    private boolean schoolReplyDel;

    private String createdDate;

    private List<String> pictureList;

    private boolean confirmStatus;

    private List<ReplyPlusNewObject> replyList;
}
