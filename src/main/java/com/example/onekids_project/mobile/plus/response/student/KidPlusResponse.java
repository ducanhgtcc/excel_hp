package com.example.onekids_project.mobile.plus.response.student;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidPlusResponse extends IdResponse {

    private String avatar;

    private String nameParent;

    private String loginStatus;

    private String kidName;

    private String nickName;

}
