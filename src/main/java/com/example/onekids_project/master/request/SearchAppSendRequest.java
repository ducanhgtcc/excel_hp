package com.example.onekids_project.master.request;

import com.example.onekids_project.common.AppSendConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SearchAppSendRequest extends PageNumberWebRequest {
    @NotNull
    private Long idAgent;

    private Long idSchool;

    private String title;

    @StringInList(values = {AppSendConstant.TYPE_SYS, AppSendConstant.TYPE_COMMON, AppSendConstant.TYPE_BIRTHDAY, AppSendConstant.TYPE_CELEBRATE})
    private String sendType;

    @Override
    public String toString() {
        return "SearchAppSendRequest{" +
                "idAgent=" + idAgent +
                ", idSchool=" + idSchool +
                ", title='" + title + '\'' +
                ", sendType='" + sendType + '\'' +
                "} " + super.toString();
    }
}
