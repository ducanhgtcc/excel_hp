package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.response.base.TotalResponse;
import com.example.onekids_project.response.parentdiary.MessageParentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListHistorySmsSendResponse extends TotalResponse {

    List<historySmsSendNewResponse> responseList;
}
