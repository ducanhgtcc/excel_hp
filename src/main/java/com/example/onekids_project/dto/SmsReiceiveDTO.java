package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.entity.user.SmsCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SmsReiceiveDTO extends IdDTO {

    private Long idSend;

    private Long idUserReceiver;

    private String phone;

    private Long idSchool;

//    private SmsSend smsSend;
    private String title;

    // tên người nhận
    private String nameUser;

    private String statusError;

    private SmsCode smsCode;

    private List<SmsCode> receiversResponseList;

}
