package com.example.onekids_project.mobile.teacher.request.medicine;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherReplyMedcRequest extends IdRequest {

    private String teacherReply;

    @Override
    public String toString() {
        return "UpdateTeacherReplyMedcRequest{" +
                "teacherReply='" + teacherReply + '\'' +
                "} " + super.toString();
    }
}
