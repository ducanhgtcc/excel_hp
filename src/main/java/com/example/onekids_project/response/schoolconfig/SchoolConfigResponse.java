package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.request.schoolconfig.SchoolConfigRequest;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@Setter
public class SchoolConfigResponse extends SchoolConfigRequest {

    private SysConfigShowResponse sysConfigShowResponse;
}
