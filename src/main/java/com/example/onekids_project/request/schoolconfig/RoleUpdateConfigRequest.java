package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class RoleUpdateConfigRequest extends IdRequest {
    @NotBlank
    private String roleName;

    private String description;

    List<Long> idApiList;
}
