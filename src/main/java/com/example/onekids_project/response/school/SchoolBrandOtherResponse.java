package com.example.onekids_project.response.school;

import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolBrandOtherResponse extends IdResponse {

    private AgentOtherResponse agent;
}
