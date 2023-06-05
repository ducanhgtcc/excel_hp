package com.example.onekids_project.response.school;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * date 2021-03-05 15:27
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class SchoolInfoResponse {
    private String bankInfo;

    private String expired;

    private String note;
}
