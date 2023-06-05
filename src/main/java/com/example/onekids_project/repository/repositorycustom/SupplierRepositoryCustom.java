package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.request.brand.SearchSmsConfigRequest;

import java.util.List;
import java.util.Optional;

public interface SupplierRepositoryCustom {

    List<Supplier> searchSmsConfig( SearchSmsConfigRequest searchSmsConfigRequest);

    Optional<Supplier> findByIdSupplier( Long id);

    List<Supplier> findAllsupplier();

}


