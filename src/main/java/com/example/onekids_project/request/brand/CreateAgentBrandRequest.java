package com.example.onekids_project.request.brand;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateAgentBrandRequest {

    private Long idBrand;

    private Long idAgent;
}
