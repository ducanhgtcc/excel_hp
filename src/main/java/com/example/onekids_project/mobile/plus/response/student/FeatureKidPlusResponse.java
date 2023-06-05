package com.example.onekids_project.mobile.plus.response.student;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureKidPlusResponse extends IdResponse {
    private String avatarKid;

    private String nameClass;

    private String nameParent;

    private String loginStatus;

    private String nameKid;
}
