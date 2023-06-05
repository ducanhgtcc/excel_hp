package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.request.brand.SearchBrandConfigRequest;

import java.util.List;
import java.util.Optional;

public interface BrandRepositoryCustom {

    List<Brand> searchBrandconfig(SearchBrandConfigRequest searchBrandConfigRequest);

    Optional<Brand> findByIdBrand(Long id);

    List<Brand> findAllBrand();
}


