package com.example.onekids_project.response.agent;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentUniqueResponse extends IdResponse {
    private String agentName;
}
