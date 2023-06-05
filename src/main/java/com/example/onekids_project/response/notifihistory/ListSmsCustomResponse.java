package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSmsCustomResponse extends TotalResponse {

    List<SmsCustomNewResponse> responseList;
}
