package com.example.onekids_project.mobile.teacher.request.notifyTeacher;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherSendReplyRequest extends IdRequest {

    private String teacherReply;

    @Override
    public String toString() {
        return "UpdateTeacherSendReplyRequest{" +
                "teacherReply='" + teacherReply + '\'' +
                "} " + super.toString();
    }
}
