package com.example.onekids_project.response.system;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class IconTeacherConfigResponse  extends IdResponse {
    private boolean message;

    private boolean medicine;

    private boolean absent;

    private boolean health;

    private boolean attendance;

    private boolean album;

    private boolean evaluate;

    private boolean studentFees;

    private boolean video;

    private boolean learn;

    private boolean menu;

    private boolean birthday;

    private boolean camera;

    private boolean utility;

    private boolean salary;

    private boolean facebook;

    private boolean feedback;

    private boolean news;
    
    //an hien
    private boolean messageShow;

    private boolean medicineShow;

    private boolean absentShow;

    private boolean healthShow;

    private boolean attendanceShow;

    private boolean albumShow;

    private boolean evaluateShow;

    private boolean studentFeesShow;

    private boolean videoShow;

    private boolean learnShow;

    private boolean menuShow;

    private boolean birthdayShow;

    private boolean cameraShow;

    private boolean utilityShow;

    private boolean salaryShow;

    private boolean facebookShow;

    private boolean feedbackShow;

    private boolean newsShow;

    //ten
    private String messageName;

    private String medicineName;

    private String absentName;

    private String healthName;

    private String attendanceName;

    private String albumName;

    private String evaluateName;

    private String studentFeesName;

    private String videoName;

    private String learnName;

    private String menuName;

    private String birthdayName;

    private String cameraName;

    private String utilityName;

    private String salaryName;

    private String facebookName;

    private String feedbackName;

}

