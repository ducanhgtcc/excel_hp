package com.example.onekids_project.security.payload;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSuccessTeacherResponse extends IdResponse {
    private String token;

    private String avatar;
}
