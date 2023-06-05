package com.example.onekids_project.response.notifihistory;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSmsSendHistoryResponse {
    List<SmsSendHistoryResponse> smsSendHistoryResponses;
}
