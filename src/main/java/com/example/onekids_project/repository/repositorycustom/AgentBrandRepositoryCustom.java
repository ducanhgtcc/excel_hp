package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.request.brand.SearchAgentBrandRequest;

import java.util.List;
import java.util.Optional;

public interface AgentBrandRepositoryCustom {

    List<Brand> searchAgentBrand(Long idSchoolLogin, SearchAgentBrandRequest searchAgentBrandRequest);

    Optional<Brand> findByIdBrand(Long idSchoolLogin, Long id);
}
