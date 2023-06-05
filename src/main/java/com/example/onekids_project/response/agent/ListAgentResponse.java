package com.example.onekids_project.response.agent;

import com.example.onekids_project.dto.AgentDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAgentResponse {
    List<AgentResponse> agentList;
}
