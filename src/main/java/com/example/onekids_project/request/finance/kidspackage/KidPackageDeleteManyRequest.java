package com.example.onekids_project.request.finance.kidspackage;

import com.example.onekids_project.mobile.request.DateNotNullRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lavanviet
 */
@Data
public class KidPackageDeleteManyRequest extends DateNotNullRequest {
    @NotEmpty
    private List<Long> idKidList;
    @NotEmpty
    private List<Long> idPackageList;

}
