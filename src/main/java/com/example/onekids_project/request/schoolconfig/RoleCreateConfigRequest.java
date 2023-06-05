package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RoleCreateConfigRequest {
    @NotBlank
    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String type;

    @NotBlank
    private String roleName;

    private String description;

    List<Long> idApiList;
}
