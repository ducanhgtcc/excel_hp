package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReiceiverSmsSchedule extends IdResponse {

    private String reiceiverName;

    private String phone;

    private String content;

}
