package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DetailsmsPlusObject extends IdResponse {

    private String fullName;

    private String avatar;

    private boolean status;

}
