package com.example.onekids_project.request.classes;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-05-05 17:21
 *
 * @author lavanviet
 */
@Data
public class DayOffClassUpdateRequest extends IdRequest {
    @NotBlank
    private String note;
}
