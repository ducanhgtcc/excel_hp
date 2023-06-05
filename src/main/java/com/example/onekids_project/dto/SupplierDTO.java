package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.response.brandManagement.CreatesupplierResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SupplierDTO extends IdDTO {

    private String supplierName;

    private String supplierLink;

    private String usernameLink;

    private String passwordLink;

    private String supplierNote;

    @JsonBackReference
    private List<CreatesupplierResponse> createsupplierResponseList;
}
