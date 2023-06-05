package com.example.onekids_project.master.response.agent;

import com.example.onekids_project.master.response.MaUserOtherResponse;
import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AgentMasterResponse extends IdResponse {

    private String fullName;

    private LocalDate birthDay;

    private String phone;

    private String email;

    private String gender;

    private String note;

    private AgentOtherResponse agent;

    private MaUserOtherResponse maUser;
}
