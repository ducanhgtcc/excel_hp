package com.example.onekids_project.request.user;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-04-22 14:37
 *
 * @author lavanviet
 */
@Data
public class AppTypeRequest {
    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String type;
}
