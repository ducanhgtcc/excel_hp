package com.example.onekids_project.mobile.plus.request.historyNotifiRequest;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SearchSmsPlusRequest extends PageNumberRequest {

    @StringInList(values = {AppConstant.SMS, AppConstant.CUSTOM,AppConstant.SMS_ALL})
    @NotBlank
    private String smsType;

    private String content;


    @Override
    public String toString() {
        return "SearchSmsPlusRequest{" +
                "smsType='" + smsType + '\'' +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}

