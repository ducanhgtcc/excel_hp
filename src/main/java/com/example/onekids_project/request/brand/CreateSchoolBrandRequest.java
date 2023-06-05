package com.example.onekids_project.request.brand;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateSchoolBrandRequest extends IdRequest {

    @NotNull
    private Long idBrand;

    private Long idSchool;
}
