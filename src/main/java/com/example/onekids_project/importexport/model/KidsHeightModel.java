package com.example.onekids_project.importexport.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-05-20 10:10 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class KidsHeightModel {

    private Double height;

    private LocalDate timeHeight;

    private String appType;

    private boolean delActive;

    private String createdBy;
}
