package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-05-21 15:19
 *
 * @author lavanviet
 */
@Getter
@Setter
public class SchoolDataResponse extends IdResponse {
    private String schoolName;

    private String schoolDescription;

    private String schoolAddress;

    private String schoolPhone;

    private String schoolEmail;

    private String schoolWebsite;

    private String schoolAvatar;

    private String contactPhone1;

    private String contactName1;

    private String contactPhone2;

    private String contactName2;

    private String contactPhone3;

    private String contactName3;

    private String verifyCode;

    private Long smsTotal;

    private Long smsUsed;;
}
