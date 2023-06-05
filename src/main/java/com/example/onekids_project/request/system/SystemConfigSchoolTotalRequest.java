package com.example.onekids_project.request.system;

import com.example.onekids_project.request.schoolconfig.SchoolConfigRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SystemConfigSchoolTotalRequest {
    @Valid
    private SchoolConfigRequest schoolConfigInSysResponse;

    @Valid
    private SysConfigRequest sysConfigResponse;

    @NotNull
    private Long idSchool;
}
