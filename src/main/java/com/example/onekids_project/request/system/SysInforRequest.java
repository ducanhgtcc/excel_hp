package com.example.onekids_project.request.system;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@Setter
public class SysInforRequest extends IdRequest {
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

    @NotBlank
    private String verificationCode;

    private boolean schoolTrialStatus;

    @Min(1)
    private int schoolTrailNumber;

    private boolean accountTrialStatus;

    @Min(0)
    private int accountTrailNumber;

    @Min(0)
    private int limitDevicePlusNumber;

    @Min(0)
    private int limitDeviceTeacherNumber;

    @Min(0)
    private int limitDeviceParentNumber;

    private String oneCamePicture;
}
