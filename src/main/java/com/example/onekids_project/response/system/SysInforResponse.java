package com.example.onekids_project.response.system;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.time.LocalTime;

@Getter
@Setter
public class SysInforResponse extends IdResponse {
    private String namePhone1;

    private String phone1;

    private String namePhone2;

    private String phone2;

    private String namePhone3;

    private String phone3;

    private String urlApiBackend;

    private int widthAlbum;

    private int widthOther;

    private int mobileSizePage;

    private String localUrl;

    private String webUrl;

    private String urlPictureSystem;

    private String urlPictureSchool;

    private String urlPictureEmployee;

    private String urlPictureParent;

    private String urlPictureKids;

    private String urlPictureSystemLocal;

    private String urlPictureSchoolLocal;

    private String urlPictureEmployeeLocal;

    private String urlPictureParentLocal;

    private String urlPictureKidsLocal;

    private String policyLink;

    private String guideLink;

    private String guideTeacherLink;

    private String guidePlusLink;

    private String supportLink;

    private String videoLink;

    private LocalTime timeSend;

    private String verificationCode;

    private boolean schoolTrialStatus;

    private int schoolTrailNumber;

    private boolean accountTrialStatus;

    private int accountTrailNumber;

    private int limitDevicePlusNumber;

    private int limitDeviceTeacherNumber;

    private int limitDeviceParentNumber;

    private String oneCamePicture;
}
