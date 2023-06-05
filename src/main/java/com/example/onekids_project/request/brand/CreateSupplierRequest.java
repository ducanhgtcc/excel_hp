package com.example.onekids_project.request.brand;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateSupplierRequest {
    @NotBlank
    private String supplierName;

    @NotBlank
    private String supplierLink;

    @NotBlank
    private String usernameLink;

    private boolean brandTypeCskh;

    private boolean brandTypeAds;

    @NotBlank
    private String passwordLink;

    private String supplierNote;
}
