package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryNotifiPlusResponse extends IdResponse {

    private String fullName;

    private String content;

    private String avatar;

    private int numberFile;

    private int numberPicture;

    private String createdDate;

    private boolean isApprove;

    @JsonIgnore
    private String type;
}
