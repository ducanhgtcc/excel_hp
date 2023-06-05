package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BrandDTO extends IdDTO {

    private LocalDateTime createdDate;

    private String brandName;

    private boolean brandTypeCskh;

    private boolean brandTypeAds;

    private String brandType;

    private String note;
}
