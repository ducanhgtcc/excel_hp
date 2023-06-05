package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateAccountAgentResponse extends IdResponse {

    private String username;

    private String password;

    private Long idAgent;

    private Boolean agentActive;

    private String fullName;

    private LocalDate birthday;

    private String phone;

    private String email;

    private String gender;

    private String note;
}
