package com.example.onekids_project.request.brand;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBrandRequest extends IdRequest {

    private String brandName;

    private boolean brandTypeCskh;

    private boolean brandTypeAds;

    private String note;

    private boolean brandActive;

}
