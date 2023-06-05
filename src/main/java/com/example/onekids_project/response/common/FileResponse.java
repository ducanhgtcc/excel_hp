package com.example.onekids_project.response.common;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse extends IdResponse {
    private String name;

    private String url;
}
