package com.example.onekids_project.mobile.teacher.request.absent;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherReplyAbsenttcRequest extends IdRequest {

    private String teacherReply;

    private boolean teacherReplyDel;
}
