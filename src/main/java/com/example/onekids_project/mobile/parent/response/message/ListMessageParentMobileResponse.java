package com.example.onekids_project.mobile.parent.response.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMessageParentMobileResponse {

    private boolean lastPage;

    private List<MessageParentMobileResponse> messageList;
}
