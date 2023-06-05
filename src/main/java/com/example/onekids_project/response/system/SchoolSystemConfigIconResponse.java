package com.example.onekids_project.response.system;

import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolSystemConfigIconResponse extends IdResponse {
    private String schoolName;

    private boolean schoolActive;

    private AgentOtherResponse agent;
}
