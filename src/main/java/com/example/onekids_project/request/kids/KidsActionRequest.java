package com.example.onekids_project.request.kids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsActionRequest extends IdRequest {
    private String kidCode;

    private String fullName;
}
