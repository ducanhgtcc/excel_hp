package com.example.onekids_project.security.payload;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaUserActiveRequest extends IdRequest {
    private boolean activated;
}
