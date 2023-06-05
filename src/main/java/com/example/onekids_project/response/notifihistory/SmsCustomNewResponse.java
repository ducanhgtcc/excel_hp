package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SmsCustomNewResponse extends IdResponse {

    private String sendContent;//

    private LocalDateTime createdDate;//

    private Integer receivedCount;

    private Integer coutSms;//

    private String createdBy; //

    private Integer coutSuccess;

    private Integer coutAll;

    private Integer coutFail;

    private List<SmsReceiversCustom> smsReceiversCustomList;

}
