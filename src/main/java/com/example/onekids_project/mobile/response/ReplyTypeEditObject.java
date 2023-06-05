package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyTypeEditObject {
    //parent, teacher, plus
    private String type;

    //true có thể chỉnh sửa, false là không
    private boolean editStatus;

    private String fullName;

    private String avatar;

    private String content;

    private LocalDateTime createdDate;

    private boolean modifyStatus;

    private boolean revoke;
}
