package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.common.EvaluateConstant;
import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class EvaluateAttachFileResponse extends IdResponse {

    private String lastModifieBy;

    private String name;

    @Column(length = 1000, nullable = false)
    private String url;

}
