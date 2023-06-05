package com.example.onekids_project.response.brandManagement;

import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.agent.Brand;
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
public class AgentBrandResponse extends IdResponse {

    private String agentName;

    private List<Brand> brandList;

}
