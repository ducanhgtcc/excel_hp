package com.example.onekids_project.request.brand;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateBrandRequest   {

    @NotBlank
    private String brandName;

    private boolean brandTypeCskh;

    private boolean brandTypeAds;

    private String note;

    @NotNull
    private Long idSupplier;
}
