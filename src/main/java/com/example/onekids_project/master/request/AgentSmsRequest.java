package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AgentSmsRequest {

    private long smsAdd;

    private String content;

    private List<Long> idAgentList;
}
