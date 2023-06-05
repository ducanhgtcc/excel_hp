package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaUserOtherResponse extends IdResponse {
    private String username;

    private String passwordShow;

    private boolean activated;
}
