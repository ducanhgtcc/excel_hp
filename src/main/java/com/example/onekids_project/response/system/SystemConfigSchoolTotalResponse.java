package com.example.onekids_project.response.system;

import com.example.onekids_project.request.schoolconfig.SchoolConfigRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemConfigSchoolTotalResponse {
    private SchoolConfigRequest schoolConfigInSysResponse;

    private SysConfigResponse sysConfigResponse;

    private Long idSchool;
}
