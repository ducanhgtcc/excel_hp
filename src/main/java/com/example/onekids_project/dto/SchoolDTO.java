package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.brandManagement.BrandOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SchoolDTO extends IdDTO {

    private String schoolAvatar;

    private String schoolName;

    private String schoolDescription;

    private String schoolAddress;

    private String schoolPhone;

    private String schoolEmail;

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

    private Long smsBudget;

    private LocalDateTime smsBudgetDate;

    private boolean smsActiveMore;

    private Long smsUsed;

    private Long smsTotal;

    private String idsmsBrand;

    private String namePhone1;

    private String namePhone2;

    private String namePhone3;

    private LocalDate demoStart;

    private LocalDate demoEnd;

    private LocalDate dateContractStart;

    private LocalDate dateContractEnd;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private boolean limitTime;

    private boolean limitDevice;

    private int numberDevice;

    private Long smsRemain;

    private long countStudy;

    public Long getSmsRemain() {
        return this.smsTotal - this.smsUsed;
    }

    private AgentOtherResponse agent;

    private BrandOtherResponse brand;

    private LocalDateTime createdDate;

    private String groupType;
}
