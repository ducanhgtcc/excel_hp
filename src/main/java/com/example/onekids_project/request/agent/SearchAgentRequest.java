package com.example.onekids_project.request.agent;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAgentRequest extends BaseRequest {

    private String agentName;

    private Boolean activeOrUnActive;
}
