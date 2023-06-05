package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.BrandDTO;
import com.example.onekids_project.request.brand.CreateBrandRequest;
import com.example.onekids_project.request.brand.SearchBrandConfigRequest;
import com.example.onekids_project.request.brand.UpdateBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandResponse;
import com.example.onekids_project.response.brandManagement.ListBrandResponse;

import java.util.Optional;

public interface BrandService {

    ListBrandResponse searchBrandConfig(SearchBrandConfigRequest searchBrandConfigRequest);

    Optional<BrandDTO> findByIdBrand(Long id);

    boolean deleteBrand(Long id);

    BrandResponse updateBrand(Long id, UpdateBrandRequest updateBrandRequest);

    ListBrandResponse findAllBrand();

    BrandResponse createBrand(CreateBrandRequest createBrandRequest);

}
