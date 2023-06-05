package com.example.onekids_project.mobile.teacher.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherReplyRequest extends IdRequest {

    private String teacherReply;

    @Override
    public String toString() {
        return "UpdateTeacherReplyRequest{" +
                "teacherReply='" + teacherReply + '\'' +
                "} " + super.toString();
    }
}
