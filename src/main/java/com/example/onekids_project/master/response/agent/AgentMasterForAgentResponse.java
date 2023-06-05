package com.example.onekids_project.master.response.agent;

import com.example.onekids_project.master.response.MaUserOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentMasterForAgentResponse extends IdResponse {
    private String fullName;

    private String phone;

    private String email;

    private String gender;

    private String note;

    private MaUserOtherResponse maUser;
}
