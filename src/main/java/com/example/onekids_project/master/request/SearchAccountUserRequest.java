package com.example.onekids_project.master.request;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAccountUserRequest extends BaseRequest {

    private Long idSchool;

//    private Long idAgent;

    private String typeShow;
}