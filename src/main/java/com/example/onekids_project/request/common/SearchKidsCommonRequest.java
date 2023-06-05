package com.example.onekids_project.request.common;

import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * date 2021-02-24 16:57
 * 
 * @author lavanviet
 */
@Data
public class SearchKidsCommonRequest {
    @NotBlank
    @StringInList(values = {KidsStatusConstant.STUDYING, KidsStatusConstant.STUDY_WAIT, KidsStatusConstant.RESERVE, KidsStatusConstant.LEAVE_SCHOOL})
    private String status;

    @NotNull
    private Long idClass;

    private String fullName;
}
