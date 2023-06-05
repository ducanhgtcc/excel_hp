package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.SupplierDTO;
import com.example.onekids_project.request.brand.CreateSupplierRequest;
import com.example.onekids_project.request.brand.SearchSmsConfigRequest;
import com.example.onekids_project.request.brand.UpdateSupplierRequest;
import com.example.onekids_project.response.brandManagement.ListSmsLinkConfigResponse;
import com.example.onekids_project.response.brandManagement.ListSupplierResponse;
import com.example.onekids_project.response.brandManagement.SupplierResponse;

import java.util.Optional;

public interface SupplierService {

    ListSmsLinkConfigResponse searchSmsLinkConfig(SearchSmsConfigRequest searchSmsConfigRequest);

    Optional<SupplierDTO> findByIdSupplier(Long id);

//    SupplierResponse createSupplier(UserPrincipal principal, CreateSupplierRequest createSupplierRequest);

    boolean deleteSupplier(Long id);

    SupplierResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest);

    ListSupplierResponse findAllsupplie();

    SupplierResponse createSupplier1(CreateSupplierRequest createSupplierRequest);

}
