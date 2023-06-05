package com.example.onekids_project.response.agent;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AgentResponse extends IdResponse {

    private String agentName;

    private String agentEmail;

    private String agentPhone;

    private boolean agentActive;

    private String agentAddress;

    private String agentWebsite;

    private String agentDescription;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private long smsBudget;

    private LocalDateTime smsBudgetDate;

    private boolean smsActiveMore;

    private long smsUsed;

    private long smsTotal;

    private LocalDate demoStart;

    private LocalDate demoEnd;

    private LocalDate dateContractStart;

    private LocalDate dateContractEnd;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private LocalDate dateActive;

    private LocalDate dateUnactive;

    private String website;

    private long smsRemain;

//    private String schoolNameShow;
//
//    private String brandNameShow;
//
//    private List<SchoolResponse> schoolResponses;
//
//    private List<Brand> brandList;

    public long getSmsRemain(){
        return this.smsTotal-this.smsUsed;
    }
}
