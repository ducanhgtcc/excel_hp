package com.example.onekids_project.mobile.teacher.response.account;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountTeacherResponse {
    private String teacherName;

    private String avatar;

    private List<String> departmentNameList;

    private List<String> classNameList;

    private String email;

    private String phone;

    private String guideLink;
}
