package com.example.onekids_project.mobile.plus.request.historyNotifiRequest;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppSendConstant;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DetailSmsRequest extends PageNumberRequest {

    @StringInList(values = {AppConstant.SMS, AppConstant.CUSTOM})
    @NotNull
    private String key;

    @Override
    public String toString() {
        return "DetailSmsRequest{" +
                "key='" + key + '\'' +
                "} " + super.toString();
    }
}
