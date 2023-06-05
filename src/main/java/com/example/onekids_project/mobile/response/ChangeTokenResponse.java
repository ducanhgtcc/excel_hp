package com.example.onekids_project.mobile.response;

import com.example.onekids_project.common.AppConstant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTokenResponse {
    private boolean sameSchool = AppConstant.APP_TRUE;

    private String token;
}
