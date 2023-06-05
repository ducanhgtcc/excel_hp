package com.example.onekids_project.response.appsend;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAppSendNewResponse extends TotalResponse {
    List<AppSendResponse> responseList;
}
