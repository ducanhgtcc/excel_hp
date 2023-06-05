package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HistoryNotifiUserResponse  {

    private String kidName;

    private String avatarkid;

    private String statusSend;

    private Long idRevoke;
}
