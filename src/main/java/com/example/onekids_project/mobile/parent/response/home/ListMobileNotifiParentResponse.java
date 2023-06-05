package com.example.onekids_project.mobile.parent.response.home;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMobileNotifiParentResponse {
    private boolean lastPage;

    private List<MobileNotifiParentResponse> notifilList;
}
