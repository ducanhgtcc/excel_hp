package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

/**
 * date 2021-03-06 15:59
 *
 * @author lavanviet
 */
@Data
public class SearchKidsPackageDetailRequest extends DateNotNullRequest {
//    @NotBlank
//    @StringInList(values = {FinanceConstant.PAST_MONTH, FinanceConstant.NOW_MONTH, FinanceConstant.NEXT_MONTH})
//    private String monthInput;

    @Column(nullable = false)
    private Long idClass;
}
