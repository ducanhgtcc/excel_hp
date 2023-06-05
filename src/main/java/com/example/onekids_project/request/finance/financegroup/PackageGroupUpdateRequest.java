package com.example.onekids_project.request.finance.financegroup;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-06-01 16:15
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class PackageGroupUpdateRequest extends IdRequest {
    @NotBlank
    private String name;

    private String note;
}
