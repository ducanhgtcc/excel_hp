package com.example.onekids_project.mobile.response.onecam;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class CamOneModel {
    //hãng
    private String dvrType;
    //tk đầu ghi
    private String adminDvrAcc;
    //mk
    private String adminDvrPassword;
    //tên miền
    private String schoolDomain;
    //port
    private String camPort;
    private String deviceSN;
    private String portDVR;

//    -----camera

    private String camName;
    private String camChanel;
    private String camStream;
    private String linkCam;
}
