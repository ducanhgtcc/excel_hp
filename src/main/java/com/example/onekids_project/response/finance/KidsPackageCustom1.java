package com.example.onekids_project.response.finance;

import com.example.onekids_project.model.finance.NameActiveModel;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsPackageCustom1 extends IdResponse {

    private boolean active;

    private PackageOtherObject fnPackage;

    private NameActiveModel fnPackageKidsExtend;
}
