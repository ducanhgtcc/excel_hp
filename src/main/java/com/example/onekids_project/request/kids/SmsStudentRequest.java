package com.example.onekids_project.request.kids;

import com.example.onekids_project.common.SmsConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-05-28 11:09
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class SmsStudentRequest {

    @NotBlank
    @StringInList(values = {SmsConstant.SMS_GRAGE, SmsConstant.SMS_CLASS, SmsConstant.SMS_KID, SmsConstant.SMS_GROUP, SmsConstant.SMS_EMPLOYEE, SmsConstant.SMS_DEPARTMENT})
    private String type;

    @NotEmpty
    private List<Long> idList;

    @NotBlank
    private String sendContent;

    private LocalDateTime dateTime;

    private boolean timer;
}
