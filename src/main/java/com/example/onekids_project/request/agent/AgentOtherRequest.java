package com.example.onekids_project.request.agent;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AgentOtherRequest extends IdRequest {
    @NotBlank
    private String agentName;
}
