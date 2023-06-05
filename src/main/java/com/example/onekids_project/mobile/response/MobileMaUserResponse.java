package com.example.onekids_project.mobile.response;

import com.example.onekids_project.common.AppConstant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileMaUserResponse{
    private String avatar;

    private String fullName;

    private String phone;

    private String email;

    private String guideLink= AppConstant.GUIDE_LINK;
}
