package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailSmsResponse extends IdResponse {

    private String content;

    private String phone;

    private String key;

    private boolean lastPage;

    private List<DetailsmsPlusObject> dataList;
}
