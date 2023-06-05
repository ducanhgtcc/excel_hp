package com.example.onekids_project.mobile.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileFileTeacher extends IdResponse {
    private String name;

    private String url;
}
