package com.example.onekids_project.response.user;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse extends IdResponse {
    private String apiName;

    private String apiDescription;

    private int level1;

    private int level2;

    private int level3;

    private boolean used;
}
