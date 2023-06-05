package com.example.onekids_project.mobile.teacher.response.home;

import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.mobile.response.HomeIconResponse;
import com.example.onekids_project.mobile.response.LinkResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomeTeacherResponse {
    private String schoolName;

    private String teacherName;

    private Long idSchoolLogin;

    private Long idClassLogin;

    private String linkFacebook;

    private String avatar;

    private LinkResponse dataLink;

    private ChangeTokenResponse dataToken;

    private List<HomeClassResponse> classList;

    private HomeTeacherExtraResponse homeTeacherExtra;

    private List<HomeIconResponse> iconList;

    private List<HomeBirthdayResponse> birthdayList;
}
