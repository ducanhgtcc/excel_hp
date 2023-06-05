package com.example.onekids_project.response.school;

import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SchoolResponse extends IdResponse {
    private String schoolCode;

    private String schoolAvatar;

    private String schoolName;

    private String schoolDescription;

    private String schoolAddress;

    private String schoolPhone;

    private String schoolEmail;

    private String contactName;

    private String contactDescription;

    private String contactPhone;

    private String contactEmail;

    private String schoolWebsite;

    private String contactName1;

    private String contactDescription1;

    private String contactPhone1;

    private String contactEmail1;

    private String contactName2;

    private String contactDescription2;

    private String contactPhone2;

    private String contactEmail2;

    private String contactName3;

    private String contactDescription3;

    private String contactPhone3;

    private String contactEmail3;

    private boolean schoolActive;
// ngân sách sms
    private long smsBudget;

    private LocalDateTime smsBudgetDate;
// được vượt ngân sách?
    private boolean smsActiveMore;

    private long smsUsed;

    // số sms được cấp
    private long smsTotal;
    // số sms còn lại
    private long smsRemain;

    private String idsmsBrand;

    private LocalDate demoStart;

    private LocalDate demoEnd;

    private LocalDate dateContractStart;

    private LocalDate dateContractEnd;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private boolean limitTime;

    private boolean limitDevice;

    private int numberDevice;

    private boolean trialStatus;

    private AgentOtherResponse agent;

    public long getSmsRemain(){
        return this.smsTotal-this.smsUsed;
    }
}
