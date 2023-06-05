package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.BrandDTO;
import com.example.onekids_project.request.brand.CreateAgentBrandRequest;
import com.example.onekids_project.request.brand.SearchAgentBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandResponse;
import com.example.onekids_project.response.brandManagement.ListBrandResponse;

import java.util.Optional;

public interface AgentBrandService {

    ListBrandResponse searchAgentBrand(Long idSchoolLogin, SearchAgentBrandRequest searchAgentBrandRequest);

    Optional<BrandDTO> findByIdBrand(Long idSchoolLogin, Long id);

    BrandResponse createAgentBrand(Long idSchoolLogin, CreateAgentBrandRequest createAgentBrandRequest);
}
