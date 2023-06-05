package com.example.onekids_project.response.brandManagement;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponse extends IdResponse {

    private String supplierName;

    private String supplierLink;

    private String usernameLink;

    private String passwordLink;

    private String supplierNote;
}
