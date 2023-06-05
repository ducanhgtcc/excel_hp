package com.example.onekids_project.mobile.parent.response.home;

import com.example.onekids_project.mobile.response.BirthdayMobileResponse;
import com.example.onekids_project.mobile.response.HomeIconResponse;
import com.example.onekids_project.mobile.response.LinkResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomeParentResponse {
    private String schoolName;

    private Long idKidLogin;

    private String linkFacebook;

    private LinkResponse dataLink;

    private List<BirthdayMobileResponse> birthDayList;

    private HomeParentExtraResponse homeParentExtra;

    private List<HomeKidsInforResponse> homeKidsInforResponseList;

    private List<HomeIconResponse> homeIconParentResponseList;
}
