package com.example.onekids_project.response.caskinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolInfoBankResponses extends IdResponse {

    private String bankInfo;

    private String expired;

    private String note;

    private String feesParent;


}
