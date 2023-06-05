package com.example.onekids_project.response.brandManagement;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FindAgentBrandResponse extends IdResponse {

    private String agentName;

    private List<BrandOtherResponse> brandList;

}
