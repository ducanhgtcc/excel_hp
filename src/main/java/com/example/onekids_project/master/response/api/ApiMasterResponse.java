package com.example.onekids_project.master.response.api;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiMasterResponse extends IdResponse {
    private String apiName;

    private String apiDescription;

    private int level1;

    private int level2;

    private int level3;

}
