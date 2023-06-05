package com.example.onekids_project.response.brandManagement;

import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BrandResponse extends IdResponse {

    private LocalDateTime createdDate;

    private String brandName;

    private boolean brandTypeCskh;

    private boolean brandTypeAds;

    private String brandType;

    private String note;

    private Long idSupplier;

    private Long  idSchool;

    private Long idAgent;

    @JsonBackReference
    private School school;

    private String supplierNameShow;

    @JsonBackReference
    private Supplier supplier;

    @JsonBackReference
    private List<Agent> agentList;

    private String agentNameshow;

    private String schoolNameShow;

    private AgentOtherResponse agent;
}
