package com.example.onekids_project.response.school;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppIconResponse {
    private String iconName;

    private boolean status;

    private boolean statusShow;

    //trạng thái khóa của trường
    private boolean rootLockStatus;
}
