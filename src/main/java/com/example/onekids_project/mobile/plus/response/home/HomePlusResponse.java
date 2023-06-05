package com.example.onekids_project.mobile.plus.response.home;

import com.example.onekids_project.mobile.response.HomeIconResponse;
import com.example.onekids_project.mobile.response.LinkResponse;
import com.example.onekids_project.mobile.teacher.response.home.HomeBirthdayResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomePlusResponse {
    private Long idSchoolLogin;

    private LinkResponse dataLink;

    private List<SchoolPlusResponse> schoolList;

    private KidsStatisticalResponse kidsStatistical;

    private List<HomeBirthdayResponse> birthdayList;

    private HomePlusExtraResponse homePlusExtra;

    private List<HomeIconResponse> iconList;
}
