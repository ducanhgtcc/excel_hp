package com.example.onekids_project.response.finance.fnpackage;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lavanviet
 */
@Data
public class PackageRootRequest extends IdRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String unit;

    private String description;
}
