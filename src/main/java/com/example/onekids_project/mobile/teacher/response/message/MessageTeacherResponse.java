package com.example.onekids_project.mobile.teacher.response.message;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageTeacherResponse extends IdResponse {

    private String fullName;

    private String content;

    private String createdDate;

    private int replyNumber;

    private int pictureNumber;

    private String avatar;

    private boolean confirmStatus;

    private boolean teacherUnread;

}
