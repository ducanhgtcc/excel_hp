package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class KidsBirthdayDTO extends IdDTO {

    private String fullName;

    private LocalDate birthDay;

    private String kidPhone;

    private boolean isActivated;

    private boolean delActive;

    private boolean smsReceive;

    @JsonBackReference
    private MaClassDTO maClass;

    private MaClassDTO maClassResponse;

    private List<AppSendResponse> appSendList;

//    private List<ReceiversResponse> receiversResponseList;
}
