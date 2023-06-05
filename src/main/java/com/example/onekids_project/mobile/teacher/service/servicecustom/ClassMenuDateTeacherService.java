package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.menuclass.MenuDateTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ClassMenuDateTeacherService {
    List<MenuDateTeacherResponse> findDateMenu(UserPrincipal userPrincipal, LocalDate localDate);

    List<Integer> findClassMenuMonthList(UserPrincipal principal, LocalDate localDate);
}
