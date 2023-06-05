package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.response.appsend.ReceiversResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class KidsDTO extends IdDTO {

    private String kidCode;

    private String avatarKid;

    private String firstName;

    private String lastName;

    private String fullName;

    private LocalDate birthDay;

    private String nickName;

    private String gender;

    private String address;

    private String note;

    private String kidStatus;

    private LocalDate dateStart;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private String kidPhone;

    private boolean isActivated;

    private boolean delActive;

    private boolean smsReceive;

//    @JsonBackReference
//    private MaClassOtherResponse maClass;

    private MaClassOtherResponse maClass;

//    private List<AppSendResponse> appSendList;


//    private List<ReceiversResponse> receiversResponseList;

//    private List<ReceiversResponse> receiversResponseList;

//    private String passwordKids;

}
