package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.dto.base.IdDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AgentDTO extends IdDTO {

    private String agentCode;

    private String agentName;

    private String agentWebsite;

    private String agentEmail;

    private String agentPhone;

    private String agentAddress;

    private String agentDescription;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private Long smsBudget;

    private LocalDateTime smsBudgetDate;

    private boolean smsActiveMore;

    private Long smsUsed;

    private Long smsTotal;

    @JsonBackReference
    private List<SchoolDTO> schoolList;

    private List<SchoolDTO> schoolListResponse;

}
