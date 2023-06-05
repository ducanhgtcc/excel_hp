package com.example.onekids_project.master.request;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SearchFeedBackOneKidsRequest extends PageNumberWebRequest {
    @NotNull
    private Long idAgent;

    private Long idSchool;

    private boolean deleteStatus;

    @StringInList(values = {AppTypeConstant.SYSTEM, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String accountType;

    @Override
    public String toString() {
        return "SearchFeedBackOneKidsRequest{" +
                "idAgent=" + idAgent +
                ", idSchool=" + idSchool +
                ", accountType='" + accountType + '\'' +
                "} " + super.toString();
    }
}