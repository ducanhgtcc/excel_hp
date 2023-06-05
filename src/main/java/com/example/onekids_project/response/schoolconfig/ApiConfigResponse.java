package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiConfigResponse extends IdResponse {
    private String apiName;

    private String apiDescription;

    private boolean used;
}
