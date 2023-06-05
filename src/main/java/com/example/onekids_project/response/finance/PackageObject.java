package com.example.onekids_project.response.finance;

import com.example.onekids_project.model.common.NameModel;
import com.example.onekids_project.model.finance.NameActiveModel;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageObject extends IdResponse {
    private String name;

    private boolean active;

    private Long idClass;

    private PackageOtherObject fnPackage;

    private NameActiveModel fnPackageDefaultExtend;
}
