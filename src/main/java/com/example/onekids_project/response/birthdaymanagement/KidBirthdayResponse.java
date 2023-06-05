package com.example.onekids_project.response.birthdaymanagement;

import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ReceiversResponse;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class KidBirthdayResponse extends IdResponse {

    private String fullName;

    private LocalDate birthDay;

    private String gender;

    private String kidPhone;

    private MaClassOtherResponse maClass;
//
    @JsonIgnore
    private List<AppSendResponse> appSendList;

    @JsonIgnore
    private List<ReceiversResponse> receiversList;

    private int number;

    private String YearsOld;
}
