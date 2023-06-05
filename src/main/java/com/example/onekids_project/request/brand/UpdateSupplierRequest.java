package com.example.onekids_project.request.brand;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSupplierRequest extends IdRequest {

    private String supplierName;

    private String supplierLink;

    private String usernameLink;

    private String passwordLink;

    private String supplierNote;
}
