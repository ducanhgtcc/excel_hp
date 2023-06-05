package com.example.onekids_project.mobile.plus.response.absentteacher;

import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-05-31 2:33 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherPlusSendReplyResponse extends IdResponse {

    private String schoolReply;

    private List<SendReplyMobilePlusObject> replyList;
}
