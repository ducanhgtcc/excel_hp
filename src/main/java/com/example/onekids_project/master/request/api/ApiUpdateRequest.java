package com.example.onekids_project.master.request.api;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-04-24 10:57
 *
 * @author lavanviet
 */
@Data
public class ApiUpdateRequest extends IdRequest {
    @NotBlank
    private String apiName;

    private String apiDescription;
}
