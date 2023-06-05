package com.example.onekids_project.request.agent;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateAgentRequest extends IdRequest {

    private String agentName;

    private String agentEmail;

    private String agentPhone;

    private String agentAddress;

    private String agentDescription;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private Long smsBudget;

    private LocalDate demoStart;

    private LocalDate demoEnd;

    private LocalDate dateContractStart;

    private LocalDate dateContractEnd;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private boolean agentActive;

    private boolean smsActiveMore;

    private String website;

    private AgentOtherRequest agent;
}
