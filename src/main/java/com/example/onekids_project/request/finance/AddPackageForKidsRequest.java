package com.example.onekids_project.request.finance;

import com.example.onekids_project.mobile.request.DateNotNullRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author lavanviet
 */
@Data
public class AddPackageForKidsRequest extends DateNotNullRequest {
    @NotEmpty
    private List<Long> idKidList;

    @NotEmpty
    private List<Long> idPackageList;
}
