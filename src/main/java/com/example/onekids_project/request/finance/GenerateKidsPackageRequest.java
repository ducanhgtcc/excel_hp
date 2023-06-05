package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GenerateKidsPackageRequest extends DateNotNullRequest {
    @NotEmpty
    @Valid
    private List<IdObjectRequest> idKidList;
}
